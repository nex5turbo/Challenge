package com.young.challenge.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.young.challenge.R
import com.young.challenge.databinding.ActivityAddChallengeBinding
import com.young.challenge.room.MyDatabase
import com.young.challenge.room.dao.ChallengeListDAO
import com.young.challenge.room.entity.ChallengeList
import com.young.challenge.utils.Constants.APP_NAME
import java.io.File
import java.util.*

@Suppress("DEPRECATION")
@SuppressLint("SetTextI18n")
class AddChallengeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddChallengeBinding
    private lateinit var listDAO: ChallengeListDAO

    private var name = ""
    private var startDate = 0L
    private var endDate = 0L
    private var kind = 0
    private var isAlarmOn = false
    private var isHideOn = false
    private var alarmAmPm = false
    private var alarmTime = -1
    private var alarmMinute = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_challenge)

        listDAO = MyDatabase.getInstance(this)!!.challengeListDao()

        initListener()
        initStartDate()

    }
    private fun initStartDate() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        val nowYear = calendar.get(Calendar.YEAR)
        val nowMonth = calendar.get(Calendar.MONTH)
        val nowDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(Calendar.YEAR, nowYear)
        calendar.set(Calendar.MONTH, nowMonth)
        calendar.set(Calendar.DAY_OF_MONTH, nowDay)

        binding.addChallengeStartDateButton.text = "$nowYear ${nowMonth + 1} $nowDay"
        startDate = calendar.timeInMillis
    }

    private fun initListener() {
        binding.addChallengeAddButton.setOnClickListener {
            name = binding.addChallengeNameEditText.text.toString()
            if (name.length < 2) {
                Toast.makeText(this, "2?????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isValidName(name)) {
                Toast.makeText(this, "????????? ??? ??????????????? ???????????????.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (listDAO.countName(name) > 0) {
                Toast.makeText(this, "??????????????? ???????????????.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (startDate == 0L) {
                Toast.makeText(this, "????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (endDate == 0L && kind == 1) {
                Toast.makeText(this, "????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isAlarmOn) {
                if (alarmTime == -1 || alarmMinute == -1) {
                    Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val data = ChallengeList(name, startDate, endDate, kind, isAlarmOn, isHideOn, alarmAmPm, alarmTime, alarmMinute)
            listDAO.insertChallengeList(data)
            makeDirectory(isHideOn, name)
            finish()
        }

        binding.addChallengeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0->{
                        kind = 0
                        binding.addChallengeEndDateButton.isEnabled = false
                    }

                    1->{
                        kind = 1
                        binding.addChallengeEndDateButton.isEnabled = true
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.addChallengeStartDateButton.setOnClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            val nowYear = calendar.get(Calendar.YEAR)
            val nowMonth = calendar.get(Calendar.MONTH)
            val nowDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    binding.addChallengeStartDateButton.text = "$year ${month + 1} $dayOfMonth"
                    startDate = calendar.timeInMillis
                },
                nowYear, nowMonth, nowDay).show()
        }

        binding.addChallengeEndDateButton.setOnClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            val nowYear = calendar.get(Calendar.YEAR)
            val nowMonth = calendar.get(Calendar.MONTH)
            val nowDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    binding.addChallengeEndDateButton.text = "$year ${month + 1} $dayOfMonth"
                    endDate = calendar.timeInMillis
                },
                nowYear, nowMonth, nowDay).show()
        }

        binding.addChallengeTimeButton.setOnClickListener {
            TimePickerDialog(this,
                { _, hourOfDay, minute ->
                    alarmAmPm = hourOfDay >= 11
                    alarmTime = if (hourOfDay > 12) hourOfDay-12 else hourOfDay
                    alarmMinute = minute

                    val amPmText = if (alarmAmPm) "??????" else "??????"
                    val timeText = if (alarmTime in 1..9) "0$alarmTime" else if (alarmTime == 0) "12" else alarmTime.toString()
                    val minuteText = if (alarmMinute < 10) "0$alarmMinute" else "$alarmMinute"

                    binding.addChallengeTimeButton.text = "$amPmText $timeText : $minuteText"
                }, 0, 0, false).show()
        }

        binding.addChallengeTimeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            isAlarmOn = isChecked
            binding.addChallengeTimeButton.isEnabled = isAlarmOn
        }

        binding.addChallengeHideCheckBox.setOnCheckedChangeListener { _, isChecked ->
            isHideOn = isChecked
        }
    }

    private fun makeDirectory(isHideOn: Boolean, dirName: String) {
        val mkName = if (isHideOn) ".$dirName" else dirName

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + APP_NAME + File.separator + mkName

        Log.d("###", path)
        val file = File(path)

        if (!file.mkdirs()) {
            Toast.makeText(this, "???????????? ??????", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "???????????? ??????", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidName(name: String?): Boolean {
        val trimmedName = name?.trim().toString()
        val exp = Regex("^[???-???a-zA-Z0-9._ -]{2,}\$")
        return trimmedName.isNotEmpty() && exp.matches(trimmedName)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev?.x?.toInt()
            val y = ev?.y?.toInt()
            if (!rect.contains(x!!,y!!)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
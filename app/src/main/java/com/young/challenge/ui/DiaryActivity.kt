package com.young.challenge.ui

import android.content.Intent
import android.graphics.Rect
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.young.challenge.R
import com.young.challenge.databinding.ActivityDiaryBinding
import com.young.challenge.room.entity.ChallengeItem
import com.young.challenge.utils.Constants.DIARY_CODE
import com.young.challenge.utils.Constants.DIARY_MODIFY_CODE
import com.young.challenge.utils.Display.deviceWidth

class DiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryBinding
    lateinit var data: ChallengeItem
    var code = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary)

        code = intent.getIntExtra("code", -1)
        data = intent.getSerializableExtra("data") as ChallengeItem
        val imagePath = intent.getStringExtra("path")
        val iv = binding.diaryImageView
        iv.layoutParams.width = deviceWidth
        iv.layoutParams.height = deviceWidth
        binding.diaryEditText.setText(data.diary)

        if (code == DIARY_MODIFY_CODE) {
            binding.itemDeleteButton.visibility = View.VISIBLE
        } else if (code == DIARY_CODE) {
            binding.itemDeleteButton.visibility = View.GONE
        }

        Glide.with(this)
            .load(imagePath)
            .fitCenter()
            .into(iv)
    }

    override fun onBackPressed() {
        data.diary = binding.diaryEditText.text.toString()
        val intent = Intent()
        intent.putExtra("data", data)
        setResult(RESULT_OK, intent)
        super.onBackPressed()
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
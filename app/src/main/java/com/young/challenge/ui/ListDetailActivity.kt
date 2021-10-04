package com.young.challenge.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.young.challenge.R
import com.young.challenge.adapter.ListDetailRecyclerAdapter
import com.young.challenge.databinding.ActivityListDetailBinding
import com.young.challenge.room.entity.ChallengeItem
import com.young.challenge.room.entity.ChallengeList
import com.young.challenge.utils.Constants.APP_NAME
import com.young.challenge.utils.Constants.CAMERA_CODE
import com.young.challenge.utils.Constants.DIARY_CODE
import com.young.challenge.utils.Constants.DIARY_MODIFY_CODE
import com.young.challenge.utils.DateUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class ListDetailActivity : AppCompatActivity() {
    private val viewModel: ListDetailViewModel by viewModels()
    private val binding: ActivityListDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_list_detail)
    }

    private lateinit var challengeData: ChallengeList
    private lateinit var adapter: ListDetailRecyclerAdapter

    private var tempFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        challengeData = intent.getSerializableExtra("data") as ChallengeList

        adapter = ListDetailRecyclerAdapter(challengeData, viewModel, this)
        binding.listDetailRecyclerView.adapter = adapter

        viewModel.setItemList(challengeData)

        viewModel.itemList.observe(this, {
            for (mData in it) {
                Log.d("###", mData.toString())
            }
            adapter.setData(it)
        })

        binding.cameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            tempFile = createImageFile()
            Log.d("###", tempFile!!.name)
            val imageUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", tempFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, CAMERA_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("###", "$requestCode")
        if (tempFile?.length() == 0L) {
            tempFile?.delete()
        } else {
            if (resultCode == RESULT_OK && requestCode == CAMERA_CODE) run {
                val name = tempFile!!.name
                val day = DateUtil.dateDifference(challengeData.startDate, challengeData.endDate, challengeData.kind) + 1
                val challengeName = challengeData.challengeName
                val diary = ""
                val mData = ChallengeItem(challengeName, name ,day, diary)
                val intent = Intent(this, DiaryActivity::class.java)
                intent.putExtra("data", mData)
                intent.putExtra("path", tempFile?.absolutePath)
                intent.putExtra("code", DIARY_CODE)
                startActivityForResult(intent, DIARY_CODE)
            }
        }
        if (resultCode == RESULT_OK && requestCode == DIARY_CODE) run {
            val resultData = data?.getSerializableExtra("data") as ChallengeItem
            viewModel.insertItem(resultData)
            tempFile = null
        }
        if (resultCode == RESULT_OK && requestCode == DIARY_MODIFY_CODE) run {
            val isDeleted = data?.getBooleanExtra("deleted", false)
            if (!isDeleted!!) {
                val resultData = data.getSerializableExtra("data") as ChallengeItem
                viewModel.updateItem(resultData)
            } else {
                viewModel.setItemList(challengeData)
            }

        }
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        Log.d("###", timestamp)
        val fileName = "${challengeData.challengeName}_$timestamp"
        val dirName =
            if (challengeData.isHideOn) ".${challengeData.challengeName}" else challengeData.challengeName
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .toString() + File.separator + APP_NAME + File.separator + dirName
        )
        Log.d("###", fileName)
        return File.createTempFile(
            fileName, ".jpg", storageDir
        )
    }
}
package com.young.challenge.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.young.challenge.R
import com.young.challenge.adapter.MainRecyclerAdapter
import com.young.challenge.databinding.ActivityMainBinding
import com.young.challenge.utils.Constants.APP_NAME
import com.young.challenge.utils.Display
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MainRecyclerAdapter

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        checkDirectory()
        Display.initDM(applicationContext)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = MainRecyclerAdapter(viewModel)
        binding.mainRecyclerView.adapter = adapter

        viewModel.challengeList.observe(this, { items ->
            adapter.setData(items)
        })

        binding.testButton.setOnClickListener {
            val intent = Intent(this, AddChallengeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllList()
    }

    private fun checkDirectory(){
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + APP_NAME + File.separator
        Log.d("###", path)
        val file = File(path)
        if (file.exists()) return

        if (!file.mkdirs()) {
            Toast.makeText(this, "앱 폴더 생성에 에러가 발생했습니다. 앱을 재시작해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
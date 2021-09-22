package com.young.challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.young.challenge.databinding.ActivityListDetailBinding
import com.young.challenge.room.entity.ChallengeList

class ListDetailActivity : AppCompatActivity() {
    private val viewModel: ListDetailViewModel by viewModels()
    private val binding: ActivityListDetailBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_list_detail)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        val challengeData = intent.getSerializableExtra("data") as ChallengeList

        viewModel.setItemList(challengeData)

        viewModel.itemList.observe(this, {
            for (mData in it) {
                Log.d("###", mData.toString())
            }
        })
    }
}
package com.young.challenge.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.young.challenge.room.MyDatabase
import com.young.challenge.room.entity.ChallengeItem
import com.young.challenge.room.entity.ChallengeList
import com.young.challenge.utils.DateUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ListDetailViewModel(application: Application): AndroidViewModel(application) {
    private val database = MyDatabase.getInstance(application)
    private val itemDAO = database?.challengeItemDao()

    private var _itemList: MutableLiveData<List<ChallengeItem>> = MutableLiveData(listOf())
    val itemList: LiveData<List<ChallengeItem>> = _itemList

    private var _challengeName: MutableLiveData<String> = MutableLiveData("")
    val challengeName: LiveData<String> = _challengeName

    private var _challengeDate: MutableLiveData<String> = MutableLiveData("")
    val challengeDate: LiveData<String> = _challengeDate

    fun setItemList(item: ChallengeList) {
        _challengeName.value = item.challengeName
        _challengeDate.value = getDate(item)
        CoroutineScope(IO).launch {
            val data = itemDAO?.getAllItem(item.challengeName)
            _itemList.postValue(data)
        }
    }

    fun insertItem(item: ChallengeItem) {
        CoroutineScope(IO).launch {
            itemDAO?.insertChallengeItem(item)
        }
    }

    fun getDate(item: ChallengeList): String {
        val kind = item.kind
        val endDate = item.endDate
        val startDate = item.startDate

        val dDays = DateUtil.dateDifference(startDate, endDate, kind)
        return if (item.kind == 1 && dDays == 0) {
            "D-DAY"
        } else if (kind == 1 && dDays < 0) {
            "챌린지 종료"
        } else {
            "D-${dDays + 1}"
        }
    }
}
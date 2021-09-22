package com.young.challenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.young.challenge.room.MyDatabase
import com.young.challenge.room.entity.ChallengeList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application){
    private val database = MyDatabase.getInstance(application)!!
    private val listDAO = database.challengeListDao()

    private val _challengeList: MutableLiveData<List<ChallengeList>> = MutableLiveData(listOf())
    val challengeList: LiveData<List<ChallengeList>> = _challengeList

    fun getAllList() {
        CoroutineScope(IO).launch {
            _challengeList.postValue(listDAO.getAllList())
        }
    }
}
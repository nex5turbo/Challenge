package com.young.challenge.ui

import android.app.Application
import android.view.View
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
    private val itemDAO = database.challengeItemDao()

    private val _challengeList: MutableLiveData<List<ChallengeList>> = MutableLiveData(listOf())
    val challengeList: LiveData<List<ChallengeList>> = _challengeList

    private var _noResultVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)
    val noResultVisibility: LiveData<Int> = _noResultVisibility

    fun getAllList() {
        CoroutineScope(IO).launch {
            val data = listDAO.getAllList()
            if (data.isEmpty()) {
                _noResultVisibility.postValue(View.VISIBLE)
            } else {
                _noResultVisibility.postValue(View.GONE)
            }
            _challengeList.postValue(data)
        }
    }

    fun deleteList(name: String) {
        CoroutineScope(IO).launch {
            listDAO.deleteList(name)
            itemDAO.deleteAllItem(name)
            getAllList()
        }
    }
}
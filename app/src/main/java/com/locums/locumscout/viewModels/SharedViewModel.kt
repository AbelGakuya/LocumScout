package com.locums.locumscout.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locums.locumscout.data.Hospital

class SharedViewModel: ViewModel() {
    private var _content = MutableLiveData<Hospital>()
    val content : LiveData<Hospital> = _content

    fun saveContent(hospital: Hospital){
        _content.value = hospital
    }
}
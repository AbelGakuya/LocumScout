package com.locums.locumscout.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locums.locumscout.data.ActiveLocum
import com.locums.locumscout.data.Hospital
import com.locums.locumscout.repo.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShiftsViewModel(private val
                      repository: FirebaseRepository) :
 ViewModel(){

     private val _firebaseData = MutableLiveData<List<Hospital>>()
    val firebaseData: LiveData<List<Hospital>> = _firebaseData

    fun fetchFirebaseData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = repository.getFirebaseData()

                _firebaseData.postValue(data)
            } catch (e:Exception){
                //hanlde exception
            }
        }
    }


    private val _locumsLiveData: MutableLiveData<List<ActiveLocum>> = MutableLiveData()
    val locumsLiveData: LiveData<List<ActiveLocum>> = _locumsLiveData

    //function to fetch notifications using the provided hospitalId
    fun fetchLocums(hospitalId: String?){
        viewModelScope.launch {
            val locums = repository.getActiveLocums(hospitalId)
            _locumsLiveData.postValue(locums)
        }
    }


}
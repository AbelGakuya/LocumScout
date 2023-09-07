package com.locums.locumscout.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.locums.locumscout.data.Hospital
import com.locums.locumscout.data.IncomingNotificationData

class SharedNotificationsViewModel : ViewModel() {
    private var _content = MutableLiveData<IncomingNotificationData>()
    val content : LiveData<IncomingNotificationData> = _content

    fun saveContent(notificationData: IncomingNotificationData){
        _content.value = notificationData
    }
}
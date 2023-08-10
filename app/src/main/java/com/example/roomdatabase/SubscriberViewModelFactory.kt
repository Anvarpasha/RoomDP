package com.example.roomdatabase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roomdatabase.database.SubscriberRepository

// Since we have repository as a constructor parameter, ViewModelFactory
// class also has to be repository as a constructor parameter
class SubscriberViewModelFactory(private val  repository: SubscriberRepository) : ViewModelProvider.Factory {
    // This is boilerplate code we use for every ViewModelFactory class
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if(modelClass.isAssignableFrom(SubscriberViewModel::class.java)){
          return SubscriberViewModel(repository) as T
      }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
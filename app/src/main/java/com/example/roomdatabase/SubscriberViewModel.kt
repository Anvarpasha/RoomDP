package com.example.roomdatabase

import android.util.Log
import android.util.Patterns
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdatabase.database.Subscriber
import com.example.roomdatabase.database.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//NOTE SubscriberRepositorydeki funksiyalari istifade ede bilmek ucun
// subscriberRepositoryni instance olaraq elave edirik ViewModele
class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {

    // We don't need to create function to get allSubscribers,
    // we just need to call repo's subscribers variable

    // NOTE 2.3 Burda viewmodeldə isə repositorydəki liveDatanı alırıq
    val subscribers = repository.subscribers

    //Daha sonra istifade elemek ucun iki deyishken tanimlayiriq
    private var isUpdateOrDelete = false
    private lateinit var subsUpdateOrDelete :  Subscriber

    // Status mesaji ucun mutableLiveData tanimlayiriq

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>

    get() = statusMessage


    // We define mutableLive datas for name and email
    // then we add this on activity layout on edittext's text
    val subsName = MutableLiveData<String>()
    val subsEmail = MutableLiveData<String>()

    //another two mutableLivedata for buttons
    val btnSaveOrUpdate = MutableLiveData<String>()
    val btnClearAllOrDelete = MutableLiveData<String>()

    //We define initial displaying names for these buttons
    init {
        btnSaveOrUpdate.value = "Save"
        btnClearAllOrDelete.value = "Clear All"
    }

    fun saveOrUpdate(){

        if(subsName.value==null){
            statusMessage.value =  Event("Please enter subscriber's name!")
        }
        else if(subsEmail.value==null ){
            statusMessage.value =  Event("Please enter subscriber's e-mail!")
        }else
            if(!Patterns.EMAIL_ADDRESS.matcher(subsEmail.value).matches() ){
                statusMessage.value =  Event("Please enter correct e-mail address !")
            }
        else{
                if(isUpdateOrDelete){
                    subsUpdateOrDelete.name = subsName.value!!
                    subsUpdateOrDelete.email = subsEmail.value!!
                    update(subsUpdateOrDelete)
                }else{
                    val name = subsName.value!!
                    val email = subsEmail.value!!
                    insert(Subscriber(0,name,email))
                    subsName.value = ""
                    subsEmail.value = ""
                }
        }



    }

    fun clearAllOrDelete(){
        // eger isUpdateOrDelete truedursa yeni orda herhansi bir listItem secilibse
        // delete funksiyasina secilmis subscriberin ad ve mailini gonderirik, eger hec ne secilmeyibse clear all duymesi aktiv olur
        if(isUpdateOrDelete){
            delete(subsUpdateOrDelete)
        }
        else{
            clearAll()

        }
    }

    //After these we only use functions which is located in repo

    // we did this call in background thread that is why
    // we did this call on background thread using viewModelScope
     private fun insert(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
           val newRowId = repository.insertSubscribers(subscriber)

        withContext(Dispatchers.Main){
            if(newRowId>-1){
                statusMessage.value =  Event("Subscriber Inserted Successfully $newRowId ")
            }
            else{
                statusMessage.value =  Event("Error occured")
            }
        }
        }


    private fun update(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
         val  numberOfRows =   repository.updateSubscribers(subscriber)
        withContext(Dispatchers.Main){
            if(numberOfRows>0){
                subsName.value = ""
                subsEmail.value = ""
                isUpdateOrDelete = false
                subsUpdateOrDelete = subscriber
                btnSaveOrUpdate.value = "Save"
                btnClearAllOrDelete.value = "Clear All"
                statusMessage.value =  Event("$numberOfRows Rows Updated Successfully")
            }
            else{
                statusMessage.value =  Event("Error Occured!")

            }


        }
        }


    private fun delete(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
       val numberOfRowsDeleted =  repository.deleteSubscribers(subscriber)
        //Birbasha UI ile ishlediyimiz ucun withcontext threadini istifade edirik
        withContext(Dispatchers.Main){
            if(numberOfRowsDeleted>0){
                subsName.value = ""
                subsEmail.value = ""
                isUpdateOrDelete = false
                subsUpdateOrDelete = subscriber
                btnSaveOrUpdate.value = "Save"
                btnClearAllOrDelete.value = "Clear All"
                statusMessage.value = Event("$numberOfRowsDeleted Rows Deleted Successfully!")
            }
            else{
                statusMessage.value = Event("Error Occured!")
            }

        }
    }

    private fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
       val numbOfRowsDeleted = repository.deleteAll()

        withContext(Dispatchers.Main){
            if(numbOfRowsDeleted>0){
                statusMessage.value =  Event("$numbOfRowsDeleted Rows Deleted Successfully")
            }
            else{
                statusMessage.value =  Event("Error Occured!")
            }

        }
    }

    // secilmis subscriber instancei bu funksiyaya gonderirik
    fun initUpdateAndDelete(subscriber: Subscriber){
        subsName.value = subscriber.name
        subsEmail.value = subscriber.email
        isUpdateOrDelete = true
        subsUpdateOrDelete = subscriber
        btnSaveOrUpdate.value = "Update"
        btnClearAllOrDelete.value = "Delete "
    }




}
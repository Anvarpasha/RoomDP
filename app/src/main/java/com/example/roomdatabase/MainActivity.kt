package com.example.roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabase.database.Subscriber
import com.example.roomdatabase.database.SubscriberDatabase
import com.example.roomdatabase.database.SubscriberRepository
import com.example.roomdatabase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // codes for using dataBinding object
    private lateinit var binding : ActivityMainBinding
    //reference variable for SubscriberViewModel Instance
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // using the factory class, we need to create a factory instance
        //To create a SubscriberVMFactory instance we need to pass a dao instance as an argument
        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this,factory)[SubscriberViewModel::class.java]

        //then we just assign viewModel instance to the data binding object
        binding.myViewModel = subscriberViewModel
        // because using of liveData we have to use lifecycleOwner
        binding.lifecycleOwner = this
        initRecyclerView()

        //Burda ise Toast mesaji ekranda gosteririk
        subscriberViewModel.message.observe(this){ it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initRecyclerView(){
        binding.subscriberRV.layoutManager = LinearLayoutManager(this)
        displaySubscribersList()
    }

    //NOTE 2.4 burda isə MainActivitydə o LiveDatanı observe eləmək üçün kod yazırıq
    private fun displaySubscribersList(){
       subscriberViewModel.subscribers.observe(this, Observer {
           Log.i("MYTAG",it.toString())
           // Daha sonra adapteri listOfSubscribersi elave ederek adapteri set edirik
           // burda "listItemClicked" funksiyasını arqument olaraq
           // göndərə bilmək üçün lambdadan istifadə edirik
           binding.subscriberRV.adapter = AdapterSubscribers(it,{selectedItem : Subscriber->listItemClicked(selectedItem )})
       })
    }

    // Click event for RecyclerView, burdaki subscriber list itemda kliklenen
    // subscriberi mueyyen edir
    private fun listItemClicked(subscriber: Subscriber ){
       // Toast.makeText(this,"selected subscriber's name is ${subscriber.name}",Toast.LENGTH_SHORT).show()

        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}
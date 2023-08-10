package com.example.roomdatabase.database

//NOTE we add SubscriberDAO as a parameter because we will call the functions of DAO from repo
class SubscriberRepository(private val dao : SubscriberDAO){

    // NOTE first we define a variable for LiveData of list of subscribers
    //NOTE 2.2  Burda isə livedata olaraq çəkdiyimiz datanı subscribers propertysinə atayırıq
    val subscribers = dao.getAllSubscribers()

    suspend fun insertSubscribers(subscriber: Subscriber) : Long{
       return dao.insertSubscriber(subscriber)
    }

    suspend fun updateSubscribers(subscriber: Subscriber) : Int{
       return dao.updateSubscriber(subscriber)
    }

    suspend fun deleteSubscribers(subscriber: Subscriber): Int{
       return dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll() :Int{
        return dao.deleteAll()
    }

}
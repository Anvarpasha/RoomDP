package com.example.roomdatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.database.Subscriber
import com.example.roomdatabase.databinding.ListItemSubscribersBinding
import com.example.roomdatabase.generated.callback.OnClickListener

// Bundan öncə mainActivityden funskiyanı parametr olaraq bura göndərdik deyə burda da constructoru dəyişməliyik
// Bu funksiya hec ne geri dondurmediyi ucun Unit elave edirik
class AdapterSubscribers(private val subscribersList: List<Subscriber>, private val clickListener: (Subscriber)->Unit ) :RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding :ListItemSubscribersBinding =
            DataBindingUtil.inflate(layoutInflater,R.layout.list_item_subscribers,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subscribersList.size
    }

    //We use onBindViewHolder to display data on the listItem
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // burda da reference variable kimi clickListeneri gondermek lazimdi
        holder.bind(subscribersList[position],clickListener)
    }
}


// DataBinding istifade edecik deye listitemin databinding
// objectini constructor parameter olaraq elave etmeliyik
class MyViewHolder(val binding: ListItemSubscribersBinding) : RecyclerView.ViewHolder(binding.root){

    // daha sonra adapterdə qeyd etdiyim clicklisteneri bind funksiyasına da əlavə  edirəm
    fun bind(subscriber: Subscriber,clickListener: (Subscriber)->Unit) {
        binding.subscriberName.text = subscriber.name
        binding.subscriberEmail.text = subscriber.email

        //daha sonra list item'a onclick elave edirem
        //bu clicklistener icinde subscriberin instanceini gonderecek funksiya tanimlayiram
        //Bu funkisya MainActivityde tanimlanan listItemClickede gonderecek datani
        binding.listItem.setOnClickListener {
            clickListener(subscriber)
        }
    }
}
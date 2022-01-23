package com.example.simpletodo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * transform list of strings into recyclerView list
 **/
class TaskItemAdapter(val listOfTasks : List<String>,
                      val longClickListener : OnLongClickListener,
                      val clickListener : OnClickListener) : RecyclerView.Adapter<TaskItemAdapter.ViewHolder>(){

    // implemented by obj created in MainActivity, given to TaskItemAdapter
    interface OnLongClickListener{
        fun onItemLongClicked(position: Int)
    }

    //same
    interface OnClickListener{
        fun onItemClicked(position: Int)
    }

    // Holds each view in a list element? e.g. textView in a text list item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView
        init {
            //the only view inside a simple_list_item_1 layout
            textView = itemView.findViewById(android.R.id.text1)


            itemView.setOnLongClickListener {
                // reference to a listener from MainActivity
                longClickListener.onItemLongClicked(adapterPosition)

                //STRANGE, adapterPosition always is -1
                Log.i("SimpleToDo", "user longclicked task: " + adapterPosition)
                true
            }

            itemView.setOnClickListener{
                //reference to a listener from MainActivity
                clickListener.onItemClicked(adapterPosition)

                Log.i("SimpleToDo","user clicked task:"+adapterPosition)
                true
            }
        }
    }

    // "Inflate" a XML layout, picking out each view, and return holder for easy access
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val taskView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(taskView)
    }

    // give each view the data (string) it needs
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // what is the data
        val item = listOfTasks.get(position)
        // here is the data, view
        holder.textView.text = item
    }

    override fun getItemCount(): Int {
        return listOfTasks.size
    }

}
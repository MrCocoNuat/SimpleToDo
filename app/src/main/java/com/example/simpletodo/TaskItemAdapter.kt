package com.example.simpletodo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RatingBar
//import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


/**
 * transform list of strings into recyclerView list
 **/
class TaskItemAdapter(val listOfTasks : List<Task>,
                      val deleteClickListener : OnDeleteClickListener,
                      val clickListener : OnClickListener,
                      //val switchChangeListener: SwitchChangeListener
                      ) : RecyclerView.Adapter<TaskItemAdapter.ViewHolder>(){

    // implemented by obj created in MainActivity, given to TaskItemAdapter
    interface OnDeleteClickListener{
        fun onDeleteClicked(position: Int)
    }

    //same
    interface OnClickListener{
        fun onItemClicked(position: Int)
    }
    /*
    //same but for the done switch
    interface SwitchChangeListener{
        fun onItemChecked(position: Int, newDone : Boolean)
    }
     */

    // Holds each view in a list element? e.g. textView in a text list item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val taskTextView: TextView = itemView.findViewById(R.id.taskText)
        val dateTextView: TextView = itemView.findViewById(R.id.dateText)
        //val doneSwitch: Switch
        val ratingBarView : RatingBar = itemView.findViewById(R.id.ratingBar2)
        val deleteButtonView : ImageButton = itemView.findViewById(R.id.deleteButton)

        init {
            // pull these references from the input View
            //doneSwitch = itemView.findViewById(R.id.doneSwitch)

            /*
            // long clicking the text does a delete
            taskTextView.setOnLongClickListener {
                // reference to a listener from MainActivity
                longClickListener.onItemLongClicked(adapterPosition)

                Log.i("SimpleToDo", "user long clicked task: " + adapterPosition)
                true
            }
             */

            // clicking the delete button does a delete
            deleteButtonView.setOnClickListener {
                // sadly the operation to trigger a delete is no longer a long click...
                // refactored from onItemLongClicked
                deleteClickListener.onDeleteClicked(adapterPosition)
            }

            // clicking the task at all does an edit
            taskTextView.setOnClickListener{
                //reference to a listener from MainActivity
                clickListener.onItemClicked(adapterPosition)

                Log.i("SimpleToDo","user clicked task:"+adapterPosition)
            }
            dateTextView.setOnClickListener{
                //reference to a listener from MainActivity
                clickListener.onItemClicked(adapterPosition)

                Log.i("SimpleToDo","user clicked task:"+adapterPosition)
            }
            ratingBarView.setOnClickListener{
                //reference to a listener from MainActivity
                clickListener.onItemClicked(adapterPosition)

                Log.i("SimpleToDo","user clicked task:"+adapterPosition)
            }

            /*
            // check for when the done switch is checked
            doneSwitch.setOnCheckedChangeListener{ null : null, doneSwitch.isChecked : Boolean -> (
                // back to MainActivity again
                switchChangeListener.onItemChecked(adapterPosition, doneSwitch.isChecked)

                Log.i("SimpleToDo", "user toggled switch:" + adapterPosition)
                true)
            }
             */
        }
    }

    // "Inflate" a XML layout, picking out each view, and return holder for easy access
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val taskView = inflater.inflate(R.layout.item_task, parent, false)
        return ViewHolder(taskView)
    }

    // sorry international users... but you can swap month/day's uses or change this function
    private fun taskDateFormat(month : String, day : String): String {
        return "%2s/%2s".format(month.padStart(2,'0'), day.padStart(2,'0'))
    }

    // give each view the data (string) it needs
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // what is the data (Task object)
        val task = listOfTasks[position]

        // here is the data, view
        holder.taskTextView.text = task.text
        holder.dateTextView.text = taskDateFormat(task.month, task.day)
        holder.ratingBarView.rating = task.priority.toFloat()
    }

    override fun getItemCount(): Int {
        return listOfTasks.size
    }

}
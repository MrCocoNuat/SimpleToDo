package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    // store the tasks
    var listOfTasks = mutableListOf<String>()

    //prevents circular reference
    lateinit var adapter : TaskItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load persistence
        load()

        // which recycler to put tasks in?
        val rvTasks = findViewById<RecyclerView>(R.id.recyclerTasks)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            // called by TaskItemAdapter
            override fun onItemLongClicked(position : Int){
                //remove a task
                listOfTasks.removeAt(position)
                //notify adapter
                adapter.notifyItemRemoved(position)

                //save persistence
                save()
            }
        }

        val onClickListener = object : TaskItemAdapter.OnClickListener{
            override fun onItemClicked(position: Int) {
                //intent to edit this task
                val i = Intent(this@MainActivity, EditActivity::class.java)
                //send the old task text in the bundle
                i.putExtra("oldTask",listOfTasks[position])
                // control goes out of this function, losing memory of position! Preserve by sending too
                i.putExtra("position", position)

                // Activity-specific key to get the edited task text from EditActivity
                val REQUEST_CODE = 1
                //start it up
                startActivityForResult(i, REQUEST_CODE)

                //wait for that Activity to finish and this Activity's onActivityResult to be autocalled
            }
        }

        //give adapter a OnLongClickListener to report back
        //  here be the initialization
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)

        //connect recycler to adapter
        rvTasks.adapter = adapter

        //layout manager
        rvTasks.layoutManager = LinearLayoutManager(this)


        // button and textfield references
        val button = findViewById<Button>(R.id.button)
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        button.setOnClickListener{
            //grab text from taskField
            val inputTask: String = inputTextField.text.toString()

            //stop nonsense task from being added
            if (inputTask.length > 0) {
                //append to listOfTasks
                listOfTasks.add(inputTask)
                //notify recycler adapter of update
                adapter.notifyItemInserted(listOfTasks.size - 1)
                //clear taskField
                inputTextField.text.clear()

                //save persistence
                save()

                //be responsible!
                Log.i("SimpleToDo", "User clicked button")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Activity specific to EditActivity
        val REQUEST_CODE = 1

        //check that this is from EditActivity and everything was OK
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            //data Intent cannot be null
            data!!

            // retrieve updated text from Bundle
            val newTaskText = data.getStringExtra("newTask")
            val position = data.getIntExtra("position",-1)
            Log.i("SimpleToDo","Received task: "+newTaskText+" at position "+position)
            //update listOfTasks
            if (newTaskText != null) {
                listOfTasks.set(position, newTaskText)
                Log.i("SimpleToDo","edited task successfully")
            }
            //notify adapter
            adapter.notifyItemChanged(position)

            Log.i("SimpleToDo","Returned from EditActivity to MainActivity")
            //persistence
            save()
        }
    }

    // to get a file
    fun getDataFile() : File {
        // not exposed to user

        //each line is one task,
        //so don't put any newlines in your tasks!!
        return File(filesDir, "data.txt")
    }

    // to load tasks from file into list
    fun load(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())

            Log.i("SimpleToDo","loaded from file successfully")
        } catch(ioException: IOException){
            // uh oh
            ioException.printStackTrace()
        }
    }

    // to save tasks to file from list
    fun save(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)

            Log.i("SimpleToDo","saved to file successfully")
        } catch(ioException: IOException){
            // uh oh
            ioException.printStackTrace()
        }

    }
}


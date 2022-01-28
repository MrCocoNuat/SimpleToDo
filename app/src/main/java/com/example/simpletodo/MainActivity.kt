package com.example.simpletodo



/*
   There are quite a few commented (deprecated) code blocks.
   Yes, this is probably against good coding style.
   However, I think they're worth keeping around for reference, especially as a learner.

   If you're using Android Studio, you can always just fold them.
 */


import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    // store the tasks
    var listOfTasks = mutableListOf<Task>()

    //prevents circular reference
    lateinit var adapter : TaskItemAdapter

    private fun showEditDialog(addMode : Boolean, position : Int) {
        val editDialogListener = object : EditDialogFragment.EditDialogListener{
            override fun onDialogNegativeClick(dialog: DialogFragment) {
                Log.i("SimpleToDo","Negative Button pressed")
                //do nothing, ergo...
                //identical to just canceling with back button
            }

            override fun onDialogPositiveClick(dialog: DialogFragment) {
                Log.i("SimpleToDo","Positive Button pressed")
                // Confirm edit/add task

                // cast for sure will succeed
                dialog as EditDialogFragment
                val view = dialog.dialogView

                var newText = view.findViewById<EditText>(R.id.editTextTask).text.toString()
                if (newText == ""){
                    newText = "(blank)"  //empty string absolutely not allowed!
                }
                newText = newText.replace('\n',' ') // newlines absolutely not allowed!
                var newMonth = view.findViewById<EditText>(R.id.editTextMonth).text.toString()
                if (newMonth == ""){
                    newMonth = "0"
                }
                newMonth = newMonth.replace('\n',' ')
                var newDay = view.findViewById<EditText>(R.id.editTextDay).text.toString()
                if (newDay == ""){
                    newDay = "0"
                }
                newDay = newDay.replace('\n',' ')
                val newPriority = view.findViewById<RatingBar>(R.id.ratingBar).rating.toInt()

                // add mode
                if (addMode) {
                    val newTask = Task(newText, newMonth, newDay, newPriority)
                    listOfTasks.add(newTask)
                    adapter.notifyItemInserted(listOfTasks.size - 1)
                }
                // edit mode
                else {
                    val editTask = listOfTasks[position]
                    editTask.text = newText
                    editTask.month = newMonth
                    editTask.day = newDay
                    editTask.priority = newPriority
                    adapter.notifyItemChanged(position)
                }

                Log.i("SimpleToDo","Dialog finished successfully")
                save()

            }
        }

        // if addMode, position should have no effect
        if (addMode) {
            val frag = EditDialogFragment(editDialogListener, addMode, -1, "", "", "", 0)
            frag.show(supportFragmentManager, "dialog")
        }
        else{
            val toEdit = listOfTasks[position]
            Log.i("SimpleToDo","dialog called for editing task")
            val frag = EditDialogFragment(editDialogListener, addMode, position, toEdit.text, toEdit.month, toEdit.day, toEdit.priority)
            frag.show(supportFragmentManager, "dialog")
        }

        Log.i("SimpleToDo","showed dialog")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load persistence
        load()

        // which recycler to put tasks in?
        val rvTasks = findViewById<RecyclerView>(R.id.recyclerTasks)

        val onDeleteClickListener = object : TaskItemAdapter.OnDeleteClickListener{
            // called by TaskItemAdapter
            override fun onDeleteClicked(position : Int){
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
                // Dialog in edit mode, not addMode
                showEditDialog(false, position)

                /*
                //intent to edit this task
                val i = Intent(this@MainActivity, EditActivity::class.java)
                //send the old task text in the bundle
                i.putExtra("oldTask",listOfTasks[position].text)
                // control goes out of this function, losing memory of position! Preserve by sending too
                i.putExtra("position", position)

                // Activity-specific key to get the edited task text from EditActivity
                val REQUEST_CODE = 1
                //start it up
                startActivityForResult(i, REQUEST_CODE)

                //wait for that Activity to finish and this Activity's onActivityResult to be autocalled
                 */
            }
        }

        /*
        val switchChangeListener = object : TaskItemAdapter.SwitchChangeListener{
            override fun onItemChecked(position: Int, newDone : Boolean) {
                listOfTasks[position].done = newDone
                adapter.notifyItemChanged(position)
                save()
            }
        }

         */

        //give adapter a OnLongClickListener to report back
        //  here be the initialization
        adapter = TaskItemAdapter(listOfTasks, onDeleteClickListener, onClickListener)

        //connect recycler to adapter
        rvTasks.adapter = adapter

        //layout manager
        rvTasks.layoutManager = LinearLayoutManager(this)


        // button and textfield references
        //val button = findViewById<Button>(R.id.button)
        //val inputTextField = findViewById<EditText>(R.id.addTaskField)
        val button2 = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        button2.setOnClickListener{
            //if addMode, position has no effect
            showEditDialog(true, -1)
        }

       /*
        button.setOnClickListener{
            //grab text from taskField
            val inputText: String = inputTextField.text.toString()

            val newTask = Task(inputText,"0","0",0)

            //stop nonsense task from being added
            if (inputText.length > 0) {
                //append to listOfTasks
                listOfTasks.add(newTask)
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
        */
    }

    /*
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
            if (newTaskText != null) {
                val position = data.getIntExtra("position", -1)
                Log.i("SimpleToDo", "Received task: " + newTaskText + " at position " + position)
                //update listOfTasks
                val newTask = Task(newTaskText, "0", "0", 0)

                listOfTasks.set(position, newTask)
                Log.i("SimpleToDo", "edited task successfully")
                //notify adapter
                adapter.notifyItemChanged(position)

                Log.i("SimpleToDo", "Returned from EditActivity to MainActivity")
                //persistence
                save()
            }
        }
    }
     */







    // to get a file
    fun getDataFile() : File {
        // not exposed to user

        //each line is one task,
        //so don't put any newlines in your tasks!!
        //sanitization now enforced
        return File(filesDir, "data4.txt")
    }

    // to load tasks from file into list
    fun load(){
        try {
            val rawBuffer = mutableListOf<String>()
            for (s : String in FileUtils.readLines(getDataFile(), Charset.defaultCharset())){
                if (s.isNotEmpty()){
                    rawBuffer.add(s)
                    if (rawBuffer.size == 4){
                        val newTask = Task(rawBuffer[0], rawBuffer[1], rawBuffer[2], rawBuffer[3].toInt())
                        listOfTasks.add(newTask)
                        rawBuffer.clear()
                    }
                }
            }
            Log.i("SimpleToDo","loaded from file successfully, task count of: "+listOfTasks.size)
        } catch(ioException: IOException){
            // uh oh
            ioException.printStackTrace()
        }
    }

    // to save tasks to file from list
    fun save(){
        try {
            val rawList = mutableListOf<String>()
            for (t : Task in listOfTasks) {
                rawList.addAll(arrayOf(t.text, t.month, t.day, t.priority.toString()))
            }
            FileUtils.writeLines(getDataFile(), rawList)
            //Log.i("SimpleToDo",rawList.toString())
            Log.i("SimpleToDo","saved to file successfully")
        } catch(ioException: IOException){
            // uh oh
            ioException.printStackTrace()
        }

    }
}


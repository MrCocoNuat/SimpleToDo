package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //the "finish editing" button
        val button2 = findViewById<Button>(R.id.button2)
        //the text field
        val editTask = findViewById<EditText>(R.id.editTask)

        // retrieve the old task text from the bundle
        val oldTaskText = getIntent().getStringExtra("oldTask")
        //and the position number, if -1 then very bad thing happened...
        val position = getIntent().getIntExtra("position",-1)
        // populate the edit text field
        editTask.setText(oldTaskText)

        //nothing else needs to be done

        // when finish button clicked:
        button2.setOnClickListener {
            // grab the updated text, but CONVERT WHATEVER TYPE IT IS INTO A STRING FIRST
            // THIS TOOK ME 40 MINUTES TO FIGURE OUT GAHHHHHHHHHHHHLHALDBUVBLUWD
            val newTaskText = editTask.text.toString()
            Log.i("SimpleToDo","Edited task to:"+newTaskText)

            //send data back to MainActivity with an Intent
            val data = Intent()
            // put the edited text and position into a Bundle
            data.putExtra("newTask",newTaskText)
            data.putExtra("position", position)

            // all clear! include the Bundle
            setResult(RESULT_OK, data)
            finish()

        }

    }

}
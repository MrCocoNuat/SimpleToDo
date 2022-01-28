package com.example.simpletodo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment

class EditDialogFragment(
    val editDialogListener: EditDialogListener,
    val addMode: Boolean,
    val oldText: String,
    val oldMonth: String,
    val oldDay: String,
    val oldPriority: Int
) : DialogFragment() {

    lateinit var dialogView : View

    // activity creating this dialog needs to create a listener implementing this
    interface EditDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Build what surrounds the dialog interior, title and buttons etc.
    // also fill it with a custom view
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //any view-specific listeners?

        if (this.activity != null){
            val view = activity!!.layoutInflater.inflate(R.layout.dialog_edit, null)
            //Gradle complaining about possible null value?

            //bind values
            view.findViewById<EditText>(R.id.editTextTask).setText( oldText)
            view.findViewById<EditText>(R.id.editTextMonth).setText( oldMonth)
            view.findViewById<EditText>(R.id.editTextDay).setText( oldDay)
            view.findViewById<RatingBar>(R.id.ratingBar).rating = oldPriority.toFloat()

            // as a class attribute
            dialogView = view

            val builder = AlertDialog.Builder(activity)

            val title : String = if (addMode) "Add Task" else "Edit Task"

            builder.setTitle(title)
                .setPositiveButton("Confirm") { _, _ ->
                    editDialogListener.onDialogPositiveClick(this)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    editDialogListener.onDialogNegativeClick(this)
                }
                .setView(view)

            return builder.create()
        }
        throw IllegalStateException("EditDialogFragment activity is null")
    }
}
package com.example.simpletodo

import kotlinx.parcelize.Parcelize
import android.os.Parcelable


//serialize Task to pass in an Intent
@Parcelize
class Task(
    var text: String,
    var month: String,
    var day: String,
    var priority: Int,
    //var done: Boolean
) : Parcelable
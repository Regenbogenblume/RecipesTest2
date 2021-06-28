package com.breev.v2datamodel.utils

import android.util.Log

class EasyLog(private val className: String) {

    companion object{
        const val LOG_TAG = "BreeLog"
    }

    fun d(message: String){
        Log.d(LOG_TAG, "$className: $message")
    }

}
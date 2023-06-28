package com.example.hw_db.databaseAccess

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream


class DBHelper (
    private var DB_PATH: String,
    private var DB_NAME: String,
    private val SCHEMA: Int,
    private var myContext: Context
        ) : SQLiteOpenHelper(myContext, DB_NAME, null, SCHEMA)  {
    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun createDB(){
        val file = File(DB_PATH)
        if(file.exists()){
            try {
                val myInput = myContext.assets.open(DB_NAME)
                val myOutput = FileOutputStream(DB_PATH)
                val buffer = ByteArray(1024)
                var length = myInput.read(buffer)
                while ({length = myInput.read(buffer); length}() > 0){
                    myOutput.write(buffer,0, length)
                }
                myInput.close()
                myOutput.close()
            }
            catch (ex: Exception){
                Log.e("Error", ex.message.toString())
            }
        }
    }

    fun open() : SQLiteDatabase {
        return SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READWRITE)
    }

    companion object Factory{
            fun build(context: Context): DBHelper{
                val _DB_NAME = "buylist.db"
                val _DB_PATH = context.filesDir.path + _DB_NAME
                return DBHelper(_DB_PATH, _DB_NAME, 1, context)
            }
    }

}
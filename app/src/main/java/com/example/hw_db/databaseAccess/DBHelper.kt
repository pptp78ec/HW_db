package com.example.hw_db.databaseAccess

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.edit
import com.example.hw_db.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream


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
        val file = File(DB_PATH).also { if(!it.exists()){
        it.createNewFile()}
        }
        if(file.exists() && !myContext.getSharedPreferences("DbPrefs", Context.MODE_PRIVATE).getBoolean("dbcreated", false)){
            try {
                FileOutputStream(DB_PATH).use { out ->
                    myContext.resources.openRawResource(R.raw.buylist).use{
                        it.copyTo(out)
                    }
                }
                myContext.getSharedPreferences("DbPrefs", Context.MODE_PRIVATE).edit {
                    this.putBoolean("dbcreated", true)
                    this.apply()
                }
            }
            catch (ex: Exception){
                Log.e("!!!ERROR!!", ex.message.toString() + " " + ex.toString())

                for(i in ex.stackTrace){

                    Log.e("!!!Error!!!",i.toString())
                }
            }
        }
    }

    fun open() : SQLiteDatabase {
        return SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READWRITE)
    }

    companion object Factory{
            fun build(context: Context): DBHelper{
                val _DB_NAME = "buylist.db"
                val _DB_PATH = context.filesDir.path +"""/"""+ _DB_NAME
                return DBHelper(_DB_PATH, _DB_NAME, 1, context)
            }
    }

}
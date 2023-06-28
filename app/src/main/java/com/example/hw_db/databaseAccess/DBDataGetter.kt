package com.example.hw_db.databaseAccess

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import com.example.hw_db.Model.Product

class DBDataGetter(private val DbHelper:DBHelper) {

    fun getCountTypeList():MutableList<String>{
        val db = DbHelper.open()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("""SELECT "label" FROM [Type]""", null)
            val array = arrayListOf<String>()
            do {
                array.add(cursor.getString(0))
            }while (cursor.moveToNext())
            db.close()
            cursor.close()
            return array.toMutableList()

        }
        catch (ex:Exception){
            Log.e("Error", ex.message.toString())
        }
        finally {
            if(db.isOpen){
                db.close()
            }
            if(cursor!=null && !cursor.isClosed){
                cursor.close()
            }
        }
        return mutableListOf()
    }

    fun getProductListInList(listId: Int): MutableList<Product>{
        val db = DbHelper.open()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("""SELECT Product.name, Product.count, Type.label, Product.checked, Lists.name as listname FROM [Product] JOIN [Type] on Product.count_type = Type._id JOIN [Lists] on Product.list_id = Lists._id""", null)
            val list = mutableListOf<Product>()
            do {

                list.add(Product(cursor.getString(0), cursor.getFloat(1).toString(), cursor.getString(4), (cursor.getInt(3)==1), cursor.getString(2)))
            }while (cursor.moveToNext())
            db.close()
            cursor.close()
            return list
        }
        catch (ex:Exception){
            Log.e("Error", ex.message.toString())
        }
        finally {
            if(db.isOpen){
                db.close()
            }
            if(cursor!=null && !cursor.isClosed){
                cursor.close()
            }
        }
        return mutableListOf()
    }

}
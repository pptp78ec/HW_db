package com.example.hw_db.databaseAccess

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.hw_db.Model.Buylist
import com.example.hw_db.Model.Product
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

class DBDataGetter(private val DbHelper: DBHelper) {

    fun getCountTypeList(): MutableList<String> {
        val db = DbHelper.open()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("""SELECT "label" FROM [Type]""", null)
            val array = arrayListOf<String>()
            do {
                array.add(cursor.getString(0))
            } while (cursor.moveToNext())
            db.close()
            cursor.close()
            return array.toMutableList()

        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
        return mutableListOf()
    }

    fun getProductListInList(listId: Int): MutableList<Product> {
        val db = DbHelper.open()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                """SELECT Product._id, Product.name, Product.count, Type.label, Product.checked, Lists.name as listname FROM [Product] JOIN [Type] on Product.count_type = Type._id JOIN [Lists] on Product.list_id = Lists._id WHERE Lists._id = """ + listId,
                null
            )
            val list = mutableListOf<Product>()
            do {

                list.add(
                    Product(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getFloat(2).toString(),
                        cursor.getString(5),
                        (cursor.getInt(4) == 1),
                        cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
            db.close()
            cursor.close()
            return list
        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
        return mutableListOf()
    }

    fun getBuyListsList(): MutableList<Buylist> {
        val db = DbHelper.open()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                """SELECT List._id, Lists.name, Lists.date, Lists.description FROM [Lists]""",
                null
            )
            val list = mutableListOf<Buylist>()
            do {
                list.add(
                    Buylist(
                        cursor.getLong(0),
                        cursor.getString(1),
                        LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(cursor.getLong(2)),
                            ZoneId.systemDefault()
                        ).toLocalDate(),
                        cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
            db.close()
            cursor.close()
            return list
        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
        return mutableListOf()
    }

    fun addProduct(product: Product, list_id: Long) {
        val db = DbHelper.open()
        var cursor: Cursor? = null
        val cv = ContentValues()
        try {
            var typeId = 0;
            cursor = db.rawQuery(
                """SELECT Type._id FROM [Type] WHERE Type.label = """ + product.counttype,
                null
            )
            if (cursor.moveToFirst()) {
                typeId = cursor.getInt(0)
            }
            cv.put("name", product.name)
            cv.put("count", product.count)
            cv.put("list_id", list_id)
            cv.put("count_type", typeId)
            db.insert("Product", null, cv)
            db.close()

        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
    }

    fun addList(buylist: Buylist) {
        val db = DbHelper.open()
        val cv = ContentValues()
        try {
            cv.put("name", buylist.name)
            cv.put("date",
                LocalDateTime.of(buylist.date, LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC)
                    .toEpochMilli()
            )
            cv.put("description", buylist.description)
            db.insert("Lists", null, cv)
            db.close()

        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }
        }
    }

    fun updateProduct(product: Product, list_id: Long) {
        val db = DbHelper.open()
        var cursor: Cursor? = null
        val cv = ContentValues()
        try {
            var typeId = 0;
            cursor = db.rawQuery(
                """SELECT Type._id FROM [Type] WHERE Type.label = """ + product.counttype,
                null
            )
            if (cursor.moveToFirst()) {
                typeId = cursor.getInt(0)
            }
            cv.put("name", product.name)
            cv.put("count", product.count)
            cv.put("list_id", list_id)
            cv.put("count_type", typeId)
            db.update("Product", cv, """WHERE Product._id = """ + product.id, null)
            db.close()

        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
    }

    fun updateList(buylist: Buylist) {
        val db = DbHelper.open()
        val cv = ContentValues()
        try {
            cv.put("name", buylist.name)
            cv.put("date",
                LocalDateTime.of(buylist.date, LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC)
                    .toEpochMilli()
            )
            cv.put("description", buylist.description)
            db.update("Lists", cv, """WHERE Lists._id = """ + buylist.id, null)
            db.close()

        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }
        }
    }

    fun deleteProduct(product: Product) {
        val db = DbHelper.open()
        try {
            db.delete("Product", """WHERE Product._id = """ + product.id, null)
            db.close()

        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }

        }
    }

    fun deleteList(buylist: Buylist) {
        val db = DbHelper.open()
        try {
            db.delete("Lists", """WHERE Lists._id = """ + buylist.id, null)
            db.close()

        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        } finally {
            if (db.isOpen) {
                db.close()
            }
        }
    }
}
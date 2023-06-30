package com.example.hw_db

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.hw_db.DataAdapters.BuyListDataAdapter
import com.example.hw_db.DataAdapters.ProdListDataAdapter
import com.example.hw_db.Model.Buylist
import com.example.hw_db.databaseAccess.DBDataGetter
import com.example.hw_db.databaseAccess.DBHelper

class MainActivity : AppCompatActivity() {
    var dbDataGetter: DBDataGetter? = null
    var buylistListView: ListView? = null
    var lists: MutableList<Buylist>? = null

    val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                lists = dbDataGetter?.getBuyListsList()
                (buylistListView?.adapter as BuyListDataAdapter).notifyDataSetChanged()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DBHelper.build(this).also {
            it.createDB()
            dbDataGetter = DBDataGetter(it)
            lists = dbDataGetter?.getBuyListsList()
        }
        buylistListView = findViewById<ListView>(R.id.listview_lists).also {
            it.adapter = BuyListDataAdapter(this, R.layout.listitem_list, lists!!)
            it.onItemLongClickListener = onlongpress(this, result, dbDataGetter!!)
        }
        findViewById<Button>(R.id.button_addlist).setOnClickListener({ onaddclick(this) })
    }

    fun onlongpress(contextParam: Context, result: ActivityResultLauncher<Intent>, datagetter: DBDataGetter) = object : AdapterView.OnItemLongClickListener {
        override fun onItemLongClick(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ): Boolean {
            val selectedList = parent?.getItemAtPosition(position) as Buylist
            val builder = AlertDialog.Builder(contextParam).setTitle("Actions:").also {
                it.setPositiveButton("Edit", fun(_: DialogInterface, _: Int) {
                    val intent =
                        Intent(contextParam, CreateUpdateActivityProduct::class.java).also {
                            val bundleToProdEditAct: Bundle = Bundle()
                            bundleToProdEditAct.putSerializable("editItem", selectedList)
                            it.putExtra("BUNDLE", bundleToProdEditAct)
                        }
                    result.launch(intent)
                })
                it.setNeutralButton("Delete", fun(_: DialogInterface, _: Int) {
                    datagetter.deleteList(selectedList)
                })
                it.setNeutralButton("Cancel", fun(interf: DialogInterface, _: Int) {
                    interf.cancel()
                })
            }
            builder.create()
            return true
        }
    }
    fun onaddclick(contextParam: Context) {
        val intent = Intent(contextParam, CreateUpdateActivityList::class.java)
        result.launch(intent)
    }
}
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
import com.example.hw_db.DataAdapters.ProdListDataAdapter
import com.example.hw_db.Model.Product
import com.example.hw_db.databaseAccess.DBDataGetter
import com.example.hw_db.databaseAccess.DBHelper

class ProdList : AppCompatActivity() {
    var listid: Long? = null
    var ProductsListView: ListView? = null
    var prodList: MutableList<Product>? = null
    var dbDataGetter: DBDataGetter? = null

    val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                refreshData()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_prod_list)
        listid = intent.extras?.getLong("listid")

        dbDataGetter =
            DBDataGetter(DBHelper.build(this)).also { prodList = it.getProductListInList(listid!!) }
        val adapter = ProdListDataAdapter(this, R.layout.listitem_prod, prodList!!)
        ProductsListView = findViewById<ListView>(R.id.listview_products).also {
            it.adapter = adapter
            it.onItemClickListener = onlongpress(this, listid!!, result, dbDataGetter!!)
        }
        findViewById<Button>(R.id.button_addprod).setOnClickListener({onaddclick(this)})

    }

    fun onlongpress(
        contextParam: Context,
        listid: Long,
        result: ActivityResultLauncher<Intent>,
        datagetter: DBDataGetter
    ) = object : AdapterView.OnItemClickListener {

        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val builder = AlertDialog.Builder(contextParam).setTitle("Actions:")
            val selectedProduct = parent?.getItemAtPosition(position) as Product
            builder.setPositiveButton("Edit", fun(_: DialogInterface, _: Int) {

                val intent = Intent(contextParam, CreateUpdateActivityProduct::class.java).also {
                    it.putExtra("listid", listid)
                    val bundleToProdEditAct: Bundle = Bundle()
                    bundleToProdEditAct.putSerializable("editItem", selectedProduct)
                    it.putExtra("BUNDLE", bundleToProdEditAct)
                }
                result.launch(intent)
            })
            builder.setNegativeButton("Delete", fun(_: DialogInterface, _: Int) {
                datagetter.deleteProduct(selectedProduct)
                refreshData()
            })
            builder.setNeutralButton("Cancel", fun(interf: DialogInterface, _: Int) {
                interf.cancel()
            })
            builder.create()
            builder.show()
        }


    }
    fun onaddclick(contextParam: Context) {
        val intent = Intent(contextParam, CreateUpdateActivityProduct::class.java).also {
            it.putExtra("listid", listid)
        }
        result.launch(intent)
    }

    fun refreshData() {
        prodList?.clear()
        prodList?.addAll(dbDataGetter?.getProductListInList(listid!!)!!)
        (ProductsListView?.adapter as ProdListDataAdapter).notifyDataSetChanged()
    }

}




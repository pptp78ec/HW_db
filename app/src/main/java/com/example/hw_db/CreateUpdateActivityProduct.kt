package com.example.hw_db

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import com.example.hw_db.Model.Buylist
import com.example.hw_db.Model.Product
import com.example.hw_db.databaseAccess.DBDataGetter
import com.example.hw_db.databaseAccess.DBHelper
import java.lang.Exception

class CreateUpdateActivityProduct : AppCompatActivity() {
    var bundle: Bundle? = null
    var product: Product? = null
    var listid: Long? = null
    var viewProdName: EditText? = null
    var viewProdCount: EditText? = null
    var viewCountType: Spinner? = null
    var viewCheckBox: CheckBox? = null
    var DBDataGetter: DBDataGetter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_product)
        bundle = intent.extras?.getBundle("BUNDLE")
        listid = intent.extras?.getLong("listid")
        DBDataGetter = DBDataGetter(DBHelper.build(this))
        fillFieldsWithData(bundle)
        findViewById<Button>(R.id.btn_saveprod).setOnClickListener({ clickSave() })
    }


    fun fillFieldsWithData(bundle: Bundle?) {
        if (bundle != null && !bundle.isEmpty) {
            product = bundle.getSerializable("editItem") as Product
        }
        viewProdName = findViewById<EditText>(R.id.add_prodname).also { it.setText(product?.name) }
        viewProdCount =
            findViewById<EditText>(R.id.add_count).also { it.setText(product?.count.toString()) }
        viewCheckBox = findViewById<CheckBox>(R.id.cbx_bought).also {
            it.isChecked = product?.checked == true
        }
        viewCountType = findViewById<Spinner>(R.id.spn_cnttype).also {
            it.adapter = ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                DBDataGetter!!.getCountTypeList()
            )
            if (product != null) {
                it.setSelection((it.adapter as ArrayAdapter<String>).getPosition(product!!.counttype.toString()))
            }
        }

    }

    fun clickSave() {

        try {
            val newProduct = Product(
                0,
                viewProdName?.text.toString(),
                viewProdCount?.text.toString(),
                "",
                viewCheckBox?.isChecked!!.or(false),
                viewCountType?.selectedItem.toString()
            )
            if (product != null) {
                newProduct.id = product!!.id
                DBDataGetter?.updateProduct(newProduct!!, listid!!)
            } else DBDataGetter?.addProduct(newProduct!!, listid!!)
        } catch (ex: Exception) {
            Log.e("Error", ex.message.toString())
        }
        setResult(RESULT_OK)
        finish()

    }
}
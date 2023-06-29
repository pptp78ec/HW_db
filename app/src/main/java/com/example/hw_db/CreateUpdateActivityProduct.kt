package com.example.hw_db

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.hw_db.Model.Buylist
import com.example.hw_db.Model.Product
import com.example.hw_db.databaseAccess.DBDataGetter
import com.example.hw_db.databaseAccess.DBHelper

class CreateUpdateActivityProduct : AppCompatActivity() {
    var bundle: Bundle? = null
    var product: Product? = null
    var listid: Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_product)
        bundle = intent.extras?.getBundle("BUNDLE")
        listid = intent.extras?.getLong("listid")
        if (bundle != null) {
            fillFieldsWithData(bundle!!)
        }
        findViewById<Button>(R.id.btn_saveprod).setOnClickListener(View.OnClickListener { clickSave() })
    }


    fun fillFieldsWithData(bundle: Bundle) {

        product = bundle.getSerializable("editItem") as Product
        findViewById<EditText>(R.id.add_prodname).setText(product?.name)
        findViewById<EditText>(R.id.add_count).setText(product?.count.toString())

    }

    fun clickSave() {
        val DBDataGetter = DBDataGetter(DBHelper.build(this))
        if(product != null){
            DBDataGetter.updateProduct(product!!,listid!!)
        }
        else DBDataGetter.addProduct(product!!, listid!!)
        setResult(RESULT_OK)
        finish()

    }
}
package com.example.hw_db

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.example.hw_db.Model.Buylist
import com.example.hw_db.databaseAccess.DBDataGetter
import com.example.hw_db.databaseAccess.DBHelper

class CreateUpdateActivityList : AppCompatActivity() {
    var bundle: Bundle? = null
    var list: Buylist? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_list)
        bundle = intent.extras?.getBundle("BUNDLE")
        if (bundle != null) {
            fillFieldsWithData(bundle!!)
        }
        findViewById<Button>(R.id.btn_savelist).setOnClickListener(View.OnClickListener { clickSave() })

    }

    fun fillFieldsWithData(bundle: Bundle) {
        list = bundle.getSerializable("editItem") as Buylist
        findViewById<EditText>(R.id.add_listname).setText(list?.name)
        findViewById<EditText>(R.id.add_description).setText(list?.description)
    }

    fun clickSave() {
        val DBDataGetter = DBDataGetter(DBHelper.build(this))
        if(list != null){
            DBDataGetter.updateList(list!!)
        }
        else DBDataGetter.addList(list!!)
        setResult(RESULT_OK)
        finish()

    }
}
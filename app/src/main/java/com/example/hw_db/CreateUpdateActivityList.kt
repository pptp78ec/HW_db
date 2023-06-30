package com.example.hw_db

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hw_db.Model.Buylist
import com.example.hw_db.databaseAccess.DBDataGetter
import com.example.hw_db.databaseAccess.DBHelper
import java.time.LocalDate

class CreateUpdateActivityList : AppCompatActivity() {
    var bundle: Bundle? = null
    var list: Buylist? = null
    var viewListName: EditText? = null
    var viewListDesc: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_list)
        bundle = intent.extras?.getBundle("BUNDLE")
        fillFieldsWithData(bundle)
        findViewById<Button>(R.id.btn_savelist).setOnClickListener({ clickSave() })

    }

    fun fillFieldsWithData(bundle: Bundle?) {
        if(bundle!= null && !bundle.isEmpty) {
            list = bundle.getSerializable("editItem") as Buylist
        }
       viewListName =  findViewById<EditText>(R.id.add_listname).also {  it.setText(list?.name) }
       viewListDesc =  findViewById<EditText>(R.id.add_description).also { it.setText(list?.description) }
    }

    fun clickSave() {
        val buyList = Buylist(0, viewListName?.text.toString(), LocalDate.now(), viewListDesc?.text.toString()  )
        val DBDataGetter = DBDataGetter(DBHelper.build(this))
        if(list != null){
            buyList.id = list!!.id
            DBDataGetter.updateList(buyList)
        }
        else{

            DBDataGetter.addList(buyList)
        }

        setResult(RESULT_OK)
        finish()

    }
}
package com.example.hw_db.DataAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.hw_db.Model.Product
import com.example.hw_db.R

class ProdListDataAdapter( context: Context, var resource: Int,  var objects: MutableList<Product>) :
    ArrayAdapter<Product>(context, resource, objects) {
        val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var localConvertView = convertView
        val viewHolder: ViewHolder
        if(localConvertView == null){
            localConvertView = inflater.inflate(this.resource, parent, false)
            viewHolder = ViewHolder(localConvertView)
            localConvertView.tag = viewHolder
        }
        else viewHolder = localConvertView.tag as ViewHolder

        val model = objects[position]

        viewHolder.nameText?.text = model.name
        viewHolder.countText?.text = model.count.toString()
        viewHolder.counttypeText?.text = model.counttype
        viewHolder.checkBox?.isChecked = model.checked
        viewHolder.listnameText?.text = model.listname

        return localConvertView!!
    }

    class ViewHolder {
        var nameText: TextView? = null
        var countText: TextView? = null
        var counttypeText: TextView? = null
        var checkBox: CheckBox? = null
        var listnameText: TextView? = null

        constructor(view: View){
            this.nameText = view.findViewById(R.id.listitem_prodname)
            this.countText = view.findViewById(R.id.listitem_count)
            this.counttypeText = view.findViewById(R.id.listitem_counttype)
            this.checkBox = view.findViewById(androidx.appcompat.R.id.checked)
            this.checkBox = view.findViewById(R.id.listitem_list_name)
        }
    }
}
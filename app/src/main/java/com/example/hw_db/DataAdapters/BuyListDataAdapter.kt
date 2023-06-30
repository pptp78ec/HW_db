package com.example.hw_db.DataAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.hw_db.Model.Buylist
import com.example.hw_db.R


class BuyListDataAdapter(context: Context, var resource: Int, var objects: MutableList<Buylist>) :
    ArrayAdapter<Buylist>(context, resource, objects) {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var localConVertView = convertView
        val viewHolder: ViewHolder
        if(localConVertView == null){
            localConVertView = inflater.inflate(this.resource, parent, false)
            viewHolder = ViewHolder(localConVertView)
            localConVertView.tag = viewHolder
        }
        else viewHolder = localConVertView.tag as ViewHolder

        val model = objects[position]
        viewHolder.nameText?.text = model.name
        viewHolder.dateText?.text = model.date.toString()
        viewHolder.descText?.text = model.description

        return  localConVertView!!
    }


    class ViewHolder {
        var nameText: TextView? = null
        var dateText: TextView? = null
        var descText: TextView? = null
        constructor(view: View){
            this.nameText = view.findViewById(R.id.listitem_list_name)
            this.dateText = view.findViewById(R.id.listitem_date)
            this.descText = view.findViewById(R.id.listitem_desc)
        }
    }
}
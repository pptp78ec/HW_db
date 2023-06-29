package com.example.hw_db.Model

import java.io.Serializable

data class Product(var id: Long, var name: String, var count: String, var listname: String, var checked: Boolean, var counttype: String) :Serializable

package com.example.hw_db.Model

import java.io.FileDescriptor
import java.time.LocalDate

data class Buylist(var id: Long, var name: String, var date: LocalDate, var description: String)

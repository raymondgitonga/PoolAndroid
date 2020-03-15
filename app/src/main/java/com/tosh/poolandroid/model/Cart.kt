package com.tosh.poolandroid.model

import java.sql.Timestamp

data class Cart(val userId:Int,
                val cost: Double,
                val deliveryCost:Double,
                val total: Double,
                val state: String,
                val latitude: String,
                val longitude: String,
                val createdDate: String
)

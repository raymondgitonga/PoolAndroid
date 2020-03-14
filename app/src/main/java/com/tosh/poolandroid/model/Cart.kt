package com.tosh.poolandroid.model

import java.util.*

data class Cart(val userId:Int, val cost: Double, val deliveryCost:Double, val total: Double)

data class CartResult( val result: UUID)
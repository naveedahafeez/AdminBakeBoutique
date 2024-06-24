package com.example.bakeboutique

import java.io.Serializable

data class CartItem(
    val itemName: String,
    var quantity: Int,
    val price: String,
    val imageResId: Int
) : Serializable

package com.milersattning.app

data class FavoritePlace(
    val id: String,
    val name: String,
    val address: String,
    val displayName: String = address,
    val dateAdded: String
)
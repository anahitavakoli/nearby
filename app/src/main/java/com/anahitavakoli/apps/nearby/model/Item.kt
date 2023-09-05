package com.anahitavakoli.apps.nearby.model

data class Item(
    var title: String,
    var address: String,
    var neighbourhood: String,
    var region: String,
    var type: String,
    var category: String,
    var location: Location
)

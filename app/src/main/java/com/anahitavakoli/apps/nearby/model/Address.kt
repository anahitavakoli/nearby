package com.anahitavakoli.apps.nearby.model

data class Address (
    var status: String,
    var formatted_address: String,
    var route_name: String,
    var route_type: String,
    var neighbourhood: String,
    var city: String,
    var state: String,
    var place: String,
    var municipality_zone: String,
    var in_traffic_zone: String,
    var in_odd_even_zone: String,
    var village: String,
    var county: String,
    var district: String
        )
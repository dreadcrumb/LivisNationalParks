package com.example.nationalparks.model.response

import com.google.gson.annotations.SerializedName

data class ContactResponse (
    @SerializedName("companyName") val companyName: String,
    @SerializedName("street") val street: String,
    @SerializedName("country") val country: String,
    @SerializedName("phone") val phone: String,
)
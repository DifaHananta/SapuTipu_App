package com.project.saputipuapp.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("access_token")
    val accessToken: String,

    @field:SerializedName("message")
    val message: String
)

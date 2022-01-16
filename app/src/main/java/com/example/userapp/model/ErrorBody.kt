package com.example.userapp.model

data class ErrorBody(val data: List<ErrorData>)

data class ErrorData(val field: String, val message: String)
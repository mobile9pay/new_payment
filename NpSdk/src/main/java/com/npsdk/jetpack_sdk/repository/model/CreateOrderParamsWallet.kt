package com.npsdk.jetpack_sdk.repository.model

data class CreateOrderParamsWallet(
    var url: String? = "",
    var method: String? = "",
    var amount: String? = "",
    var cardToken: String? = ""
    )
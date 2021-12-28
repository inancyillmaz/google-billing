package com.inanc.easypurchase.inapppurchase.data

import com.android.billingclient.api.SkuDetails

sealed class AppResult {
    data class Success(val itemsDetail: MutableList<SkuDetails>, val message: String) : AppResult()

    data class Fail(val message: String, val itemsDetail: SkuDetails? = null) :
        AppResult()
}
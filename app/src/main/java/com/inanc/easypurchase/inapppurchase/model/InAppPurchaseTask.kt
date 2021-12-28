package com.inanc.easypurchase.inapppurchase.model

import com.inanc.easypurchase.inapppurchase.data.AppResult

data class InAppPurchaseTask(val tasksList: List<String>, val skuType : String, val taskAction: (AppResult) -> Unit)

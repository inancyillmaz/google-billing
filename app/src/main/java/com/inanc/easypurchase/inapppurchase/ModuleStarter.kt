package com.inanc.easypurchase.inapppurchase

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener


class ModuleStarter(private val context: Context) {

    fun prepareClient(purchaseUpdateListener: PurchasesUpdatedListener): BillingClient {
        return BillingClient.newBuilder(context).enablePendingPurchases()
            .setListener(purchaseUpdateListener)
            .build()
    }
}
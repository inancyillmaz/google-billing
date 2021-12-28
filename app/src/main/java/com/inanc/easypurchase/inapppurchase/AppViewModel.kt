package com.inanc.easypurchase.inapppurchase

import androidx.lifecycle.ViewModel
import com.android.billingclient.api.BillingClient
import com.inanc.easypurchase.inapppurchase.data.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AppViewModel @Inject constructor(billingClient: BillingRepository) : ViewModel() {


    init {
        billingClient.checkUserSubs(
            listOf("subitemfirstid"),
            BillingClient.SkuType.SUBS
        ) { response ->
            when (response) {
                is AppResult.Success -> println("Result : $response")
                is AppResult.Fail ->  println("Result : $response")
            }
        }
    }
}
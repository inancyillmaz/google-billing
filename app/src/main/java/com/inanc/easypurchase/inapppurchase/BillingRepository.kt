package com.inanc.easypurchase.inapppurchase

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.inanc.easypurchase.inapppurchase.data.AppResult
import com.inanc.easypurchase.inapppurchase.model.InAppPurchaseTask
import com.inanc.easypurchase.inapppurchase.utils.extension.isNull
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class BillingRepository @Inject constructor(@ApplicationContext var context: Context) {

    private val flowResponser = MediatorLiveData<AppResult?>()
    private val misConnected = MutableLiveData(false)
    private val isConnected: Boolean
        get() = misConnected.value!!
    private lateinit var billingClient: BillingClient
    private val purchaseListener = PurchasesUpdatedListener { billingResult, purchasesList ->

    }
    private var inAppPurchaseTask: MutableLiveData<InAppPurchaseTask?> = MutableLiveData(null)

    init {
        startModule(context = context)
    }


    private fun startModule(context: Context) {
        billingClient = ModuleStarter(context).prepareClient(purchaseListener)
        startConnection()
        setUserListeners()
    }

    fun checkUserSubs(itemList: List<String>, skuType: String, block: ((AppResult) -> Unit)) {
        inAppPurchaseTask.value = InAppPurchaseTask(itemList, skuType, block)
    }

    private fun setUserListeners() {
        flowResponser.addSource(inAppPurchaseTask) {
            considerRequest(isConnected)
        }
        flowResponser.addSource(misConnected) {
            considerRequest(isConnected)
        }

        flowResponser.observeForever {
            if (it != null) {
                inAppPurchaseTask.value?.taskAction?.invoke(it)
            }
        }
    }

    private fun considerRequest(connected: Boolean) {
        if (connected.not() && inAppPurchaseTask.value.isNull()) return
        getItemDetail(
            inAppPurchaseTask.value!!.tasksList,
            inAppPurchaseTask.value!!.skuType
        )
    }

    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                misConnected.postValue(billingResult.responseCode == OK)
            }

            override fun onBillingServiceDisconnected() {
                misConnected.postValue(false)
            }
        })
    }

    private fun getItemDetail(
        subItemList: List<String>,
        skuType: String
    ) {
        if (subItemList.isNullOrEmpty()) return
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(subItemList).setType(skuType)
        billingClient.querySkuDetailsAsync(params.build()) { ab, skuDetailsList ->
            if (skuDetailsList.isNullOrEmpty()) return@querySkuDetailsAsync
            if (ab.responseCode == OK) {
                flowResponser.postValue((AppResult.Success(itemsDetail = skuDetailsList, "")))
            } else {
                flowResponser.postValue((AppResult.Fail("responseCode = ${ab.responseCode}")))
            }
        }
    }
}
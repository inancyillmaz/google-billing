package com.inanc.easypurchase.inapppurchase.utils.extension

fun <T> T.isNull(): Boolean {
    return (this == null)
}

fun <T> T.isNotNull(): Boolean {
    return (this != null)
}
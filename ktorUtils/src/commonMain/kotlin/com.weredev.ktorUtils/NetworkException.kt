package com.weredev.ktorUtils

class NetworkException(val code: Int,val description: String = "") : Exception() {
}
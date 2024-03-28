package com.weredev.app.util

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class SSLUtils {

    fun setHttpClientSSL(httpsURLConnection: HttpsURLConnection) {
        httpsURLConnection.sslSocketFactory = getSslContext()?.socketFactory
        httpsURLConnection.hostnameVerifier = HostnameVerifier { hostname, session -> true }
    }

    private fun getSslContext(): SSLContext? {
        val sslContext = SSLContext.getInstance("TLS")
            .apply {
                init(null, arrayOf(TrustAllX509TrustManager()), SecureRandom())
            }
        return sslContext
    }

    inner class TrustAllX509TrustManager : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}

        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
    }
}
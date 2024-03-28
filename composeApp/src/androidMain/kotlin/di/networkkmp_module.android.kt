package di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import com.weredev.app.util.SSLUtils

actual fun configureEngineForPlatform(disableSSLCheks: Boolean): HttpClientEngine {
    val sSLUtils = SSLUtils()
    return Android.create {
        if(disableSSLCheks) {
            sslManager = { httpsURLConnection ->
                sSLUtils.setHttpClientSSL(httpsURLConnection)
            }
        }
    }
}
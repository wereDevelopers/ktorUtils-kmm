package utils

import com.weredev.ktorUtils.KtorUtils
import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Test : KoinComponent {
    private val clientInject: HttpClient by inject()

    suspend fun remoteString(url: String): String {
        val response = withContext(Dispatchers.IO){
            KtorUtils.initClient(clientInject)
            KtorUtils.executeCall<String>(HttpMethod.Get, endpoint = url)
        }
        return response
    }
}

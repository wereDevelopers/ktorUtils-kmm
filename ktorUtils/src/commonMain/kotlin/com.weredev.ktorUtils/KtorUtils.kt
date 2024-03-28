package com.weredev.ktorUtils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

object KtorUtils {
    private lateinit var client: HttpClient

    fun initClient(client: HttpClient) { this.client = client }

    suspend inline fun<reified T> executeCall(methodRequest: HttpMethod, endpoint: String,
                                              headerParams: ArrayList<Pair<String, String>> = arrayListOf(),
                                              queryParams: ArrayList<Pair<String, String>> = arrayListOf(),
                                              pathsegment: ArrayList<Pair<String, String>> = arrayListOf(),
                                              bodyRequest: Any? = null,
                                              basicAuthNeeded :Boolean = false
    ): T {
        val response = executeRequest(
            methodRequest = methodRequest,
            endpoint = endpoint,
            headerParams = headerParams,
            queryParams = queryParams,
            pathsegment = pathsegment,
            bodyRequest = bodyRequest,

        )

        if (isSuccessfull(response.status)){
            return response.body()
        }else{
            throw NetworkException(code = response.status.value,
                description = response.status.description)
        }
    }

    suspend fun executeCallLight(methodRequest: HttpMethod, endpoint: String,
                                              headerParams: ArrayList<Pair<String, String>> = arrayListOf(),
                                              queryParams: ArrayList<Pair<String, String>> = arrayListOf(),
                                              pathsegment: ArrayList<Pair<String, String>> = arrayListOf(),
                                              bodyRequest: Any? = null
    ): Boolean{
        val response = executeRequest(
            methodRequest = methodRequest,
            endpoint = endpoint,
            headerParams = headerParams,
            queryParams = queryParams,
            pathsegment = pathsegment,
            bodyRequest = bodyRequest,
        )

        if (isSuccessfull(response.status)){
            return true
        }else{
            throw NetworkException(code = response.status.value,
                description = response.status.description)
        }
    }

    suspend fun executeRequest(methodRequest: HttpMethod, endpoint: String,
                                       headerParams: ArrayList<Pair<String, String>> = arrayListOf(),
                                       queryParams: ArrayList<Pair<String, String>> = arrayListOf(),
                                       pathsegment: ArrayList<Pair<String, String>> = arrayListOf(),
                                       bodyRequest: Any? = null): HttpResponse {
        checkEndpoint(endpoint)

        val response : HttpResponse = client.request (endpoint) {
            method = methodRequest
            contentType(ContentType.Application.Json)

            headers {
                headerParams.map {
                    append(it.first, it.second)
                }
            }

            url {
                queryParams.map {
                    parameter(it.first, it.second)
                }
                pathsegment.map {
                    appendPathSegments (it.first, it.second)
                }
            }
            if (bodyRequest!=null){
                setBody(body = bodyRequest)
            }
        }
        return response
    }


    fun isSuccessfull(status: HttpStatusCode): Boolean {
         return status.value in 200..299
    }
    fun checkEndpoint(endpoint: String){
        if (endpoint.isEmpty()){
            throw IllegalArgumentException("Endpoint is empty")
        }
    }
}
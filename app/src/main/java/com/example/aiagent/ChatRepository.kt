package com.example.aiagent

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class ChatRequest(
    @SerializedName("message")
    val message: String,
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatResponse(
    @SerializedName("reply")
    val reply: String,
    @SerializedName("timestamp")
    val timestamp: String
)

interface N8nApiService {
    @POST("webhook/ai-agent")
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse
}

class ChatRepository(private val apiService: N8nApiService) {

    suspend fun sendMessageToAI(message: String): Result<String> = runCatching {
        val request = ChatRequest(message)
        val response = apiService.sendMessage(request)
        response.reply
    }

    companion object {
        fun create(baseUrl: String): ChatRepository {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(N8nApiService::class.java)
            return ChatRepository(apiService)
        }
    }
}

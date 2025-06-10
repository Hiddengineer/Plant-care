package Networking

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

class CommandSender(
    private val httpClient: HttpClient
) {
    suspend fun sendCommand(endpoint: String) {
        try {
            val ip = "192.168.1.42" // Replace with ESP32 IP shown in Serial Monitor
            val url = "http://$ip/$endpoint"
            val response = httpClient.get(url)
            println("Response from server: ${response.status}")
        } catch (e: Exception) {
            println("Failed to send command: ${e.message}")
        }
    }
}

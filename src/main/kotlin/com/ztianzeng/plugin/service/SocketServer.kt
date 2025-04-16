package com.ztianzeng.plugin.service

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

class SocketServer {
    companion object {
        const val PORT: Int = 17335  // 设置不同于客户端的端口
    }

    private var serverSocket: ServerSocket? = null
    private val isRunning = AtomicBoolean(false)
    private var serverJob: Job? = null

    fun start() {
        if (isRunning.get()) {
            println("Socket server is already running")
            return
        }

        isRunning.set(true)
        serverJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                serverSocket = ServerSocket(PORT)
                println("Socket server started on port $PORT")

                while (isRunning.get()) {
                    try {
                        val clientSocket = serverSocket?.accept()
                        clientSocket?.let { socket ->
                            handleClient(socket)
                        }
                    } catch (e: Exception) {
                        if (isRunning.get()) {
                            println("Error accepting client connection: ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error starting socket server: ${e.message}")
            }
        }
    }

    fun stop() {
        isRunning.set(false)
        serverJob?.cancel()
        try {
            serverSocket?.close()
            serverSocket = null
            println("Socket server stopped")
        } catch (e: Exception) {
            println("Error stopping socket server: ${e.message}")
        }
    }

    private fun handleClient(socket: Socket) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                socket.use { clientSocket ->
                    val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                    val writer = PrintWriter(clientSocket.getOutputStream(), true)

                    val request = reader.readLine()
                    
                    if (request == "GET_PROJECTS") {
                        val service = ProjectMonitorService.getInstance()
                        val projects = service.getOpenProjects()
                        
                        val gson = Gson()
                        val response = gson.toJson(mapOf("projects" to projects))
                        writer.println(response)
                    }
                }
            } catch (e: Exception) {
                println("Error handling client: ${e.message}")
            }
        }
    }
} 
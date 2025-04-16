package com.ztianzeng.plugin.service

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger

@Service(Service.Level.APP)
class SocketServerService {
    private val logger = Logger.getInstance(SocketServerService::class.java)
    private val socketServer = SocketServer()

    init {
        // 服务实例化时自动启动 Socket 服务器
        logger.info("SocketServerService is initializing...")
        try {
            socketServer.start()
            logger.info("Socket server started successfully")
        } catch (e: Exception) {
            logger.error("Failed to start socket server", e)
        }
    }

    fun getSocketServer(): SocketServer {
        return socketServer
    }

    fun shutdownServer() {
        logger.info("Shutting down socket server...")
        try {
            socketServer.stop()
            logger.info("Socket server stopped successfully")
        } catch (e: Exception) {
            logger.error("Failed to stop socket server", e)
        }
    }

    companion object {
        fun getInstance(): SocketServerService {
            return ApplicationManager.getApplication().getService(SocketServerService::class.java)
        }
    }
} 
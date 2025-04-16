package com.ztianzeng.plugin.api

import com.google.gson.Gson
import com.ztianzeng.plugin.service.ProjectInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.Socket


class DevHavenClient {
    companion object {
        const val PORT: Int = 17334 // Electron 应用监听的端口
        const val HOST: String = "localhost" // Electron 应用的 host
    }


    fun reportOpenProjects(name: String, projectInfos: List<ProjectInfo>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Socket(HOST, PORT).use { socket ->
                    socket.getOutputStream().use { outputStream ->
                        // 将 settings 对象序列化为 JSON 字符串
                        val gson = Gson()
                        val requestBody =
                            mapOf("projects" to projectInfos, "name" to name, "type" to "reportOpenProjects")
                        val jsonSettings = gson.toJson(requestBody)
                        // 发送 JSON 字符串到 Electron 应用
                        outputStream.write(jsonSettings.toByteArray())
                        outputStream.flush()
                        println("Settings sent to Electron app: $jsonSettings")
                    }
                }
            } catch (e: IOException) {
                System.err.println("Error sending settings to Electron app: " + e.message)
            }
        }

    }

}

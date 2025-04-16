package com.ztianzeng.plugin

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

/**
 * 简单的Socket客户端测试
 * 
 * 使用方法：
 * 1. 启动IntelliJ IDEA插件
 * 2. 运行此测试代码
 */
fun main() {
    try {
        // 连接到Socket服务器
        val socket = Socket("localhost", 17335)
        println("Connected to socket server")

        // 创建输入输出流
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val writer = PrintWriter(socket.getOutputStream(), true)

        // 发送获取项目列表请求
        writer.println("GET_PROJECTS")
        println("Sent request: GET_PROJECTS")

        // 接收响应
        val response = reader.readLine()
        println("Received response: $response")

        // 关闭连接
        socket.close()
        println("Connection closed")
    } catch (e: Exception) {
        println("Error connecting to socket server: ${e.message}")
    }
} 
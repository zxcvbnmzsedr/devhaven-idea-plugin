package com.ztianzeng.plugin.listener

import com.intellij.ide.AppLifecycleListener
import com.ztianzeng.plugin.service.SocketServerService

class AppShutdownListener : AppLifecycleListener {
    override fun appWillBeClosed(isRestart: Boolean) {
        // 应用即将关闭，停止Socket服务器
        val socketServerService = SocketServerService.getInstance()
        socketServerService.shutdownServer()
    }
} 
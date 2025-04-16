package com.ztianzeng.plugin.listener

import com.intellij.ide.AppLifecycleListener
import com.ztianzeng.plugin.service.ProjectMonitorService
import com.intellij.openapi.diagnostic.Logger

class AppStartupListener : AppLifecycleListener {
    private val LOG = Logger.getInstance(AppStartupListener::class.java)

    override fun appStarted() {
        LOG.info("DevHaven Plugin应用已启动: 初始化中...")
        try {
            println("DevHaven Plugin应用已启动: 初始化中...")
            ProjectMonitorService.getInstance().sync()
        } catch (e: Exception) {
            LOG.error("DevHaven Plugin启动初始化失败", e)
        }
    }

    override fun appWillBeClosed(isRestart: Boolean) {
        LOG.info("DevHaven Plugin应用关闭: 执行清理...")
        println("DevHaven Plugin应用关闭: 执行清理...")
        ProjectMonitorService.getInstance().sync()
    }
}

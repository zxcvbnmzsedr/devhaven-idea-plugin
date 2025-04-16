package com.ztianzeng.plugin.startup

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.ztianzeng.plugin.service.SocketServerService

class PluginStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        // 在启动时获取服务实例，这将触发服务的初始化
        SocketServerService.getInstance()
    }
} 
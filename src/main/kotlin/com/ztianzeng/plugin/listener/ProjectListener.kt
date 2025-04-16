package com.ztianzeng.plugin.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.ztianzeng.plugin.service.ProjectMonitorService
import com.ztianzeng.plugin.service.SocketServerService

class ProjectListener : ProjectManagerListener {
    override fun projectOpened(project: Project) {
        // 项目打开时触发
        // 确保SocketServerService被初始化
        SocketServerService.getInstance()
        logProjects()
    }

    override fun projectClosed(project: Project) {
        // 项目关闭时触发
        logProjects()
    }

    private fun logProjects() {
        val service = ProjectMonitorService.getInstance()
        val projects = service.getOpenProjects()

        // 只打印项目列表，不进行主动上报
        println("Current open projects: ${projects.joinToString { it.name }}")
    }
}

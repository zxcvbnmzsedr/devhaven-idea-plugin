package com.ztianzeng.plugin.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.ztianzeng.plugin.service.ProjectMonitorService

class ProjectCloseListener : ProjectManagerListener {

    override fun projectClosing(project: Project) {
        println("项目关闭")
        ProjectMonitorService.getInstance().removeProject(project)
    }

}

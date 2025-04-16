package com.ztianzeng.plugin.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.ProjectActivity
import com.ztianzeng.plugin.service.ProjectMonitorService

class ProjectOpenListener : ProjectActivity {
    override suspend fun execute(project: Project) {
        ProjectMonitorService.getInstance().addProject(project)
    }


}

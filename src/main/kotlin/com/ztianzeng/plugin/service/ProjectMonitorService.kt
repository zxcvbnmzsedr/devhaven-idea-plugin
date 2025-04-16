package com.ztianzeng.plugin.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.startup.StartupActivity
import com.intellij.util.messages.MessageBus
import com.intellij.util.messages.Topic

@Service(Service.Level.APP)
class ProjectMonitorService {
    private val projectManager = ProjectManager.getInstance()

    fun getOpenProjects(): List<ProjectInfo> {
        return projectManager.openProjects.map { project ->
            ProjectInfo(
                name = project.name,
                basePath = project.basePath ?: "",
                isDefault = project.isDefault
            )
        }
    }

    companion object {
        fun getInstance(): ProjectMonitorService {
            return com.intellij.openapi.application.ApplicationManager.getApplication()
                .getService(ProjectMonitorService::class.java)
        }
    }
}

data class ProjectInfo(
    val name: String,
    val basePath: String,
    val isDefault: Boolean
)

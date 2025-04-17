package com.ztianzeng.plugin.service

import com.google.gson.Gson
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service(Service.Level.APP)
class ProjectMonitorService {
    private val projectManager = ProjectManager.getInstance()
    private val editInfoMap = mutableMapOf<String, EditInfo>()  // 项目路径 -> 编辑信息

    data class EditInfo(val filePath: String, val line: Int, val column: Int)

    fun removeProject(project: Project) {
        val filePath: Path = getCacheDir()
        println("Projects directory: $filePath")
        val applicationInfo = ApplicationInfo.getInstance()
        //  根据base64编码项目路径创建文件,防止操作系统的问题导致文件名乱码
        val projectFile =
            filePath.resolve(
                applicationInfo.versionName + "-" + Base64.getEncoder().encodeToString(project.basePath?.toByteArray())
            )
        if (Files.exists(projectFile)) {
            try {
                Files.delete(projectFile)
                println("Deleted file: $projectFile")
            } catch (e: IOException) {
                System.err.println("Failed to delete file: $projectFile - ${e.message}")
            }
        }
    }

    // 全量同步
    fun sync() {
        val filePath: Path = getCacheDir()
        val applicationInfo = ApplicationInfo.getInstance()
        // 删除所有applicationInfo.versionName开头的文件
        Files.list(filePath).filter { it.fileName.toString().startsWith(applicationInfo.versionName) }
            .forEach { Files.delete(it) }
        // 获取所有打开的项目
        projectManager.openProjects.forEach { addProject(it) }
    }

    fun updateEditInfo(project: Project, filePath: String, line: Int, column: Int) {
        val basePath = project.basePath ?: return
        // 存储编辑信息
        editInfoMap[basePath] = EditInfo(filePath, line, column)
        // 更新项目信息文件
        addProject(project)
    }

    fun addProject(project: Project) {
        val filePath: Path = getCacheDir()
        val applicationInfo = ApplicationInfo.getInstance()
        //  根据base64编码项目路径创建文件,防止操作系统的问题导致文件名乱码
        val projectFile = filePath.resolve(
            applicationInfo.versionName + "-" + Base64.getEncoder().encodeToString(project.basePath?.toByteArray())
        )
        
        // 检查文件是否存在，如果存在先删除
        if (Files.exists(projectFile)) {
            Files.delete(projectFile)
        }
        
        try {
            Files.createFile(projectFile)
            println("Created file: $projectFile")
            // 往文件中写入json信息
            val gson = Gson()
            val projectInfoMap = HashMap<String, Any>()
            projectInfoMap["name"] = project.name
            projectInfoMap["basePath"] = project.basePath ?: ""
            projectInfoMap["isDefault"] = project.isDefault
            projectInfoMap["ide"] = applicationInfo.versionName
            
            // 添加编辑信息
            project.basePath?.let { basePath ->
                editInfoMap[basePath]?.let { editInfo ->
                    val editInfoMap = HashMap<String, Any>()
                    editInfoMap["filePath"] = editInfo.filePath
                    editInfoMap["line"] = editInfo.line
                    editInfoMap["column"] = editInfo.column
                    projectInfoMap["editInfo"] = editInfoMap
                }
            }
            
            val json = gson.toJson(projectInfoMap)
            Files.write(projectFile, json.toByteArray())
        } catch (e: IOException) {
            System.err.println("Failed to create file: $projectFile - ${e.message}")
        }
    }

    companion object {
        fun getInstance(): ProjectMonitorService {
            return com.intellij.openapi.application.ApplicationManager.getApplication()
                .getService(ProjectMonitorService::class.java)
        }
    }

    private fun getCacheDir(): Path {
        val homeDir = System.getProperty("user.home") ?: return Paths.get("")
        val filePath: Path = Paths.get(homeDir, ".devhaven", "projects")
        // 检查目录是否存在，如果不存在则创建
        if (!Files.exists(filePath)) {
            try {
                Files.createDirectories(filePath)
                println("Created directory: $filePath")
            } catch (e: IOException) {
                System.err.println("Failed to create directory: $filePath - ${e.message}")
                return Paths.get("")
            }
        }
        return filePath
    }
}

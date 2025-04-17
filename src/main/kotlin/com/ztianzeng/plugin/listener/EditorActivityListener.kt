package com.ztianzeng.plugin.listener

import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.ztianzeng.plugin.service.ProjectMonitorService

class EditorActivityListener(private val project: Project) : FileEditorManagerListener, CaretListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        updateEditInfo(file, 1, 1)
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        event.newFile?.let { file ->
            val editor = FileEditorManager.getInstance(project).selectedTextEditor
            val caretModel = editor?.caretModel
            val line = caretModel?.primaryCaret?.logicalPosition?.line?.plus(1) ?: 1
            val column = caretModel?.primaryCaret?.logicalPosition?.column?.plus(1) ?: 1
            updateEditInfo(file, line, column)
        }
    }

    override fun caretPositionChanged(event: CaretEvent) {
        val file = FileEditorManager.getInstance(project).selectedFiles.firstOrNull() ?: return
        val line = event.newPosition.line + 1
        val column = event.newPosition.column + 1
        updateEditInfo(file, line, column)
    }

    private fun updateEditInfo(file: VirtualFile, line: Int, column: Int) {
        val filePath = file.path
        ProjectMonitorService.getInstance().updateEditInfo(project, filePath, line, column)
    }
} 
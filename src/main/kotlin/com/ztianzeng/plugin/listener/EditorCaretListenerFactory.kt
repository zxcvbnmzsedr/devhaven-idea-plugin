package com.ztianzeng.plugin.listener

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class EditorCaretListenerFactory(private val project: Project) : FileEditorManagerListener {
    private val caretListener = EditorActivityListener(project)
    
    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        val editor = source.selectedTextEditor ?: return
        registerCaretListener(editor)
    }
    
    private fun registerCaretListener(editor: Editor) {
        editor.caretModel.addCaretListener(caretListener)
    }
} 
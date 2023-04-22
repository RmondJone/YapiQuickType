package com.guohanlin.language.python

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

class PythonFileType : FileType {
    override fun getName(): String {
        return "Python File"
    }

    override fun getDescription(): String {
        return "Python Source File"
    }

    override fun getDefaultExtension(): String {
        return ".py"
    }

    override fun getIcon(): Icon? {
        return null
    }

    override fun isBinary(): Boolean {
        return false
    }

    override fun getCharset(file: VirtualFile, content: ByteArray): String? {
        return null
    }
}
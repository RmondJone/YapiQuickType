package com.guohanlin.language.python

import com.intellij.openapi.fileTypes.FileType
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

}
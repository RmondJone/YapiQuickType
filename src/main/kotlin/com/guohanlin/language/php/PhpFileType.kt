package com.guohanlin.language.php

import com.intellij.openapi.fileTypes.FileType
import javax.swing.Icon

class PhpFileType : FileType {
    override fun getName(): String {
        return "PHP File"
    }

    override fun getDescription(): String {
        return "PHP Source File"
    }

    override fun getDefaultExtension(): String {
        return ".php"
    }

    override fun getIcon(): Icon? {
        return null
    }

    override fun isBinary(): Boolean {
        return false
    }
}
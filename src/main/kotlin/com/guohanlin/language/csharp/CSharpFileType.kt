package com.guohanlin.language.csharp

import com.intellij.openapi.fileTypes.FileType
import javax.swing.Icon

/**
 * 注释：C# 文件类型定义
 * 时间：2023/4/22 13:32
 * 作者：郭翰林
 **/
class CSharpFileType : FileType {
    override fun getName(): String {
        return "C# File"
    }

    override fun getDescription(): String {
        return "C# Source File"
    }

    override fun getDefaultExtension(): String {
        return ".cs"
    }

    override fun getIcon(): Icon? {
        return null;
    }

    override fun isBinary(): Boolean {
        return false
    }
}
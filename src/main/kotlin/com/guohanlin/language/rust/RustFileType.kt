package com.guohanlin.language.rust

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

/**
 * 注释：Rust 语言定义
 * 时间：2023/4/22 13:59
 * 作者：郭翰林
 */
class RustFileType : FileType {
    override fun getName(): String {
        return "Rust File"
    }

    override fun getDescription(): String {
        return "Rust Source File"
    }

    override fun getDefaultExtension(): String {
        return ".rs"
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
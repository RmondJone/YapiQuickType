package com.guohanlin.android

import com.guohanlin.CodeStructure
import com.guohanlin.model.InterfaceDetailInfoData
import com.guohanlin.model.InterfaceResponseDTO
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

/**
 * 注释：Android Model层代码生成器
 * 时间：2021/9/1 0001 10:03
 * 作者：郭翰林
 */
class JavaModelCodeStructure(
    directory: PsiDirectory,
    data: InterfaceDetailInfoData,
    private val modelName: String,
    private val interfaceResponseDTO: InterfaceResponseDTO
) :
    CodeStructure(directory, data) {

    override fun creatCode(): String {
        var code: String = interfaceResponseDTO.info
        code = code.replaceFirst("package io.quicktype;", "package ${getPackageName()};")
        code = code.replace("package io.quicktype;", "")
        code = code.replace("public class", "public static class")
        code = code.replaceFirst("public static class", "public class")
        return code
    }

    override fun updateCode(psiFile: PsiFile) {
        val document = PsiDocumentManager.getInstance(getProject()).getDocument(psiFile)
        document?.deleteString(0, document.textLength)
        var code: String = interfaceResponseDTO.info
        code = code.replaceFirst("package io.quicktype;", "package ${getPackageName()};")
        code = code.replace("package io.quicktype;", "")
        code = code.replace("public class", "public static class")
        code = code.replaceFirst("public static class", "public class")
        document?.insertString(0, code)
    }

    override fun creatFileName(): String {
        return "${modelName}.java"
    }

    override fun creatFileType(): FileType {
        return KotlinFileType()
    }
}
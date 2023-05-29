package com.guohanlin.language.android

import com.guohanlin.CodeStructure
import com.guohanlin.model.InterfaceDetailInfoData
import com.guohanlin.model.InterfaceResponseDTO
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

/**
 * 注释：Java Model层代码生成器
 * 时间：2021/9/1 0001 10:03
 * 作者：郭翰林
 */
class JavaModelCodeStructure(
    directory: PsiDirectory,
    data: InterfaceDetailInfoData?,
    private val modelName: String,
    private val interfaceResponseDTO: InterfaceResponseDTO
) :
    CodeStructure(directory, data) {

    override fun creatCode(): String {
        return conversion(interfaceResponseDTO.info)
    }

    override fun updateCode(psiFile: PsiFile) {
        val document = PsiDocumentManager.getInstance(getProject()).getDocument(psiFile)
        document?.deleteString(0, document.textLength)
        val code: String = conversion(interfaceResponseDTO.info)
        document?.insertString(0, code)
    }

    /**
     * 注释：转换Code
     * 时间：2023/5/29 19:55
     * 作者：郭翰林
     */
    private fun conversion(code: String): String {
        var result = code
        result = result.replaceFirst(
            "package io.quicktype;",
            "package ${getPackageName()};\nimport java.util.List;"
        )
        result = result.replace("package io.quicktype;", "")
        //删除多余的public class
        result = result.replace("public class", "class")
        result = result.replaceFirst("class", "public class")
        //删除多余的import java.util.List;
        result = result.replace("import java.util.List;", "import ListPackage")
        result = result.replaceFirst("import ListPackage", "import java.util.List;")
        result = result.replace("import ListPackage", "")
        //删除多余空格
        result = result.replace("\n\n\n", "\n")
        return result
    }

    override fun creatFileName(): String {
        return "${modelName}.java"
    }

    override fun creatFileType(): FileType {
        return JavaFileType()
    }
}
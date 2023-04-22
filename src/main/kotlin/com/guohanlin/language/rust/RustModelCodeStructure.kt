package com.guohanlin.language.rust

import com.guohanlin.CodeStructure
import com.guohanlin.model.InterfaceDetailInfoData
import com.guohanlin.model.InterfaceResponseDTO
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

/**
 * 注释：Rust语言支持
 * 时间：2023/4/22 14:00
 * 作者：郭翰林
 */
class RustModelCodeStructure(
    directory: PsiDirectory,
    modelName: String,
    interfaceResponseDTO: InterfaceResponseDTO,
    data: InterfaceDetailInfoData?,
) : CodeStructure(directory,data) {

    private var fileName: String = modelName
    private var codeStr: String = interfaceResponseDTO.info

    override fun creatCode(): String {
        return codeStr
    }

    override fun updateCode(psiFile: PsiFile) {
        val document = PsiDocumentManager.getInstance(getProject()).getDocument(psiFile)
        document?.deleteString(0, document.textLength)
        document?.insertString(0, codeStr)
    }

    override fun creatFileName(): String {
        return "${fileName}.rs"
    }

    override fun creatFileType(): FileType {
        return RustFileType()
    }

}
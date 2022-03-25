package com.guohanlin.flutter

import com.guohanlin.CodeStructure
import com.guohanlin.model.InterfaceDetailInfoData
import com.guohanlin.model.InterfaceResponseDTO
import com.guohanlin.utils.StringUtils
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

/**
 * 注释：Flutter Model代码构造器
 * 时间：2021/8/31 0031 15:57
 * 作者：郭翰林
 */
class FlutterModelCodeStructure(
    directory: PsiDirectory,
    data: InterfaceDetailInfoData,
    modelName: String,
    interfaceResponseDTO: InterfaceResponseDTO
) :
    CodeStructure(directory, data) {
    private var fileName: String = StringUtils.humpToUnderscore(modelName, true)
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
        return "${fileName}.dart"
    }

    override fun creatFileType(): FileType {
        return DartFileType()
    }
}
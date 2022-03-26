package com.guohanlin.utils

import com.guohanlin.creatPsiFile
import com.guohanlin.language.flutter.FlutterModelCodeStructure
import com.guohanlin.model.InterfaceDetailInfoDTO
import com.guohanlin.model.InterfaceResponseDTO
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

/**
 * 注释：Dart Builder
 * 时间：2021/8/31 0031 19:38
 * 作者：郭翰林
 */
open class DartWriteCommandBuilder {
    private lateinit var project: Project

    open fun newBuilder(project: Project): Builder {
        this.project = project
        return Builder(this)
    }

    class Builder internal constructor(mBuilder: DartWriteCommandBuilder) {
        private lateinit var directory: PsiDirectory
        private lateinit var interfaceDetailInfo: InterfaceDetailInfoDTO
        private var modelName: String? = null
        private var interfaceResponse: InterfaceResponseDTO? = null
        private var project: Project = mBuilder.project
        private var isArrayModel: Boolean = false

        //设置Psi文件夹
        fun setPsiDirectory(directory: PsiDirectory): Builder {
            this.directory = directory
            return this
        }

        //设置接口定义信息
        fun setInterfaceDetailInfo(interfaceDetailInfo: InterfaceDetailInfoDTO): Builder {
            this.interfaceDetailInfo = interfaceDetailInfo
            return this
        }

        //设置将要生成的Model实体名称
        fun setModelName(modelName: String): Builder {
            this.modelName = modelName
            return this
        }

        //设置实体转换请求返回
        fun setInterfaceResponse(interfaceResponse: InterfaceResponseDTO): Builder {
            this.interfaceResponse = interfaceResponse
            return this
        }

        //设置返回数据是否为数组
        fun setIsArrayModel(isArrayModel: Boolean): Builder {
            this.isArrayModel = isArrayModel
            return this
        }

        //构建
        fun build() {
            WriteCommandAction.runWriteCommandAction(project) {
                //创建文件夹
                modelName?.let {
                    creatPsiFile(
                        directory,
                        FlutterModelCodeStructure(directory, interfaceDetailInfo.data, it, interfaceResponse!!)
                    )
                }
            }
        }
    }
}
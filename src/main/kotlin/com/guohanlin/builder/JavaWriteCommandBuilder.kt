package com.guohanlin.builder

import com.guohanlin.language.android.JavaModelCodeStructure
import com.guohanlin.model.InterfaceDetailInfoDTO
import com.guohanlin.model.InterfaceResponseDTO
import com.guohanlin.utils.creatPsiFile
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

/**
 * 注释：Java Builder
 * 时间：2021/8/31 0031 19:56
 * 作者：郭翰林
 */
open class JavaWriteCommandBuilder {
    private lateinit var project: Project

    open fun newBuilder(project: Project): Builder {
        this.project = project
        return Builder(this)
    }

    class Builder internal constructor(mBuilder: JavaWriteCommandBuilder) {
        private lateinit var directory: PsiDirectory
        private var interfaceDetailInfo: InterfaceDetailInfoDTO? = null
        private var modelName: String? = null
        private var interfaceResponse: InterfaceResponseDTO? = null
        private var project: Project = mBuilder.project

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

        //构建
        fun build() {
            WriteCommandAction.runWriteCommandAction(project) {
                modelName?.let {
                    creatPsiFile(
                        directory,
                        JavaModelCodeStructure(
                            directory,
                            interfaceDetailInfo?.data,
                            it,
                            interfaceResponse!!
                        )
                    )
                }
            }
        }
    }
}
package com.guohanlin.utils

import com.guohanlin.creatPsiFile
import com.guohanlin.language.oc.OcHeadCodeStructure
import com.guohanlin.language.oc.OcMainCodeStructure
import com.guohanlin.model.InterfaceDetailInfoDTO
import com.guohanlin.model.InterfaceResponseDTO
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

/**
 * 注释：OC 模块生成器
 * 时间：2022/3/26 12:32
 * 作者：郭翰林
 */
class OcWriteCommandBuilder {
    private lateinit var project: Project

    open fun newBuilder(project: Project): Builder {
        this.project = project
        return Builder(this)
    }

    class Builder internal constructor(mBuilder: OcWriteCommandBuilder) {
        private lateinit var directory: PsiDirectory
        private lateinit var interfaceDetailInfo: InterfaceDetailInfoDTO
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
                        OcHeadCodeStructure(directory, interfaceDetailInfo.data, it, interfaceResponse!!)
                    )
                    creatPsiFile(
                        directory,
                        OcMainCodeStructure(directory, interfaceDetailInfo.data, it, interfaceResponse!!)
                    )
                }
            }
        }
    }
}
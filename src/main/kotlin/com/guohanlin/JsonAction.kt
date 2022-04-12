package com.guohanlin

import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.ui.JsonToClassDialog
import com.guohanlin.utils.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import io.reactivex.schedulers.Schedulers

/**
 * 注释：Json转实体代码生成插件
 * 时间：2022/4/12 10:52 上午
 * 作者：郭翰林
 */
class JsonAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        //获取插件环境
        val project = e.getData(PlatformDataKeys.PROJECT) ?: return
        val dataContext = e.dataContext
        //当前模块
        val module = LangDataKeys.MODULE.getData(dataContext) ?: return
        //当前文件夹
        val directory = when (val navigatable = LangDataKeys.NAVIGATABLE.getData(dataContext)) {
            is PsiDirectory -> navigatable
            is PsiFile -> navigatable.containingDirectory
            else -> {
                val root = ModuleRootManager.getInstance(module)
                root.sourceRoots
                    .asSequence()
                    .mapNotNull {
                        PsiManager.getInstance(project).findDirectory(it)
                    }.firstOrNull()
            }
        } ?: return
        val dialog = JsonToClassDialog(project)
        if (dialog.showAndGet()) {
            val modelName = dialog.modelInput.text.toString()
            val selectPlatform = dialog.selectPlatform
            val jsonStr = dialog.textAreaDocument?.text ?: ""
            generateCode(modelName, jsonStr, selectPlatform, project, directory)
        }
    }

    /**
     * 注释：生成代码文件
     * 时间：2022/4/12 3:55 下午
     * 作者：郭翰林
     */
    private fun generateCode(
        modelName: String,
        jsonStr: String,
        targetLanguage: String,
        project: Project,
        directory: PsiDirectory,
    ) {
        val params = HashMap<String, String>()
        params["conversionType"] = "json"
        params["targetLanguage"] = targetLanguage
        params["className"] = modelName
        params["jsonString"] = jsonStr
        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                when (targetLanguage) {
                    "Java" -> {
                        JavaWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "Kotlin" -> {
                        KotlinWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "Dart" -> {
                        DartWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "TypeScript" -> {
                        ReactWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "C++" -> {
                        CppWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "Swift" -> {
                        SwiftWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "Objective-C" -> {
                        OcWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "Go" -> {
                        GoWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                }
            }
    }
}
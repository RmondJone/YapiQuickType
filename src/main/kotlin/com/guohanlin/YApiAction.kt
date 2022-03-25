package com.guohanlin

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.ui.SelectApiDialog
import com.guohanlin.utils.AndroidWriteCommandBuilder
import com.guohanlin.utils.FlutterWriteCommandBuilder
import com.guohanlin.utils.ReactWriteCommandBuilder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import io.reactivex.schedulers.Schedulers


/**
 * 注释：YApi代码生成插件
 * 时间：2021/5/20 0020 20:15
 * 作者：郭翰林
 */
class YApiAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialog = SelectApiDialog(false)
        if (dialog.showAndGet()) {
            //根据拿到的接口信息生成代码
            val interfaceInfo = dialog.interfaceInfo
            val projectSetting = dialog.projectSetting
            val selectPlatform = dialog.selectPlatform
            val isNeedModel = dialog.isNeedModel
            val modelName = if (isNeedModel) dialog.modelInput.text.toString() else ""
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
            val params = HashMap<String, String>()
            params["id"] = interfaceInfo._id.toString()
            params["token"] = projectSetting.projectToken
            Api.getService(ApiService::class.java, Constant.BASE_URL).getInterfaceDetail(params)
                .subscribeOn(Schedulers.io())
                .subscribe { interfaceDetail ->
                    run {
                        if (interfaceDetail.errcode == 0) {
                            val resBody: JSONObject = JSON.parseObject(interfaceDetail.data.res_body)
                            val properties: JSONObject = resBody["properties"] as JSONObject
                            val jsonSchema = properties["info"] as JSONObject
                            //接口定义返回数据是不是数组
                            val isArrayModel = jsonSchema["type"] == "array"
                            when (selectPlatform) {
                                "Android" -> {
                                    if (isNeedModel) {
                                        val params = HashMap<String, String>()
                                        params["conversionType"] = "jsonSchema"
                                        params["targetLanguage"] = "Java"
                                        params["className"] = modelName
                                        params["jsonString"] = JSON.toJSONString(jsonSchema)
                                        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
                                            .getInterfaceModel(params)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe {
                                                AndroidWriteCommandBuilder()
                                                    .newBuilder(project)
                                                    .setPsiDirectory(directory)
                                                    .setInterfaceDetailInfo(interfaceDetail)
                                                    .setInterfaceResponse(it)
                                                    .setModelName(modelName)
                                                    .setIsArrayModel(isArrayModel)
                                                    .build()
                                            }
                                    } else {
                                        AndroidWriteCommandBuilder()
                                            .newBuilder(project)
                                            .setPsiDirectory(directory)
                                            .setInterfaceDetailInfo(interfaceDetail)
                                            .setIsArrayModel(isArrayModel)
                                            .build()
                                    }
                                }
                                "Flutter" -> {
                                    if (isNeedModel) {
                                        val params = HashMap<String, String>()
                                        params["conversionType"] = "jsonSchema"
                                        params["targetLanguage"] = "Dart"
                                        params["className"] = modelName
                                        params["jsonString"] = JSON.toJSONString(jsonSchema)
                                        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
                                            .getInterfaceModel(params)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe {
                                                FlutterWriteCommandBuilder()
                                                    .newBuilder(project)
                                                    .setPsiDirectory(directory)
                                                    .setInterfaceDetailInfo(interfaceDetail)
                                                    .setModelName(modelName)
                                                    .setInterfaceResponse(it)
                                                    .setIsArrayModel(isArrayModel)
                                                    .build()
                                            }
                                    } else {
                                        FlutterWriteCommandBuilder()
                                            .newBuilder(project)
                                            .setPsiDirectory(directory)
                                            .setInterfaceDetailInfo(interfaceDetail)
                                            .setIsArrayModel(isArrayModel)
                                            .build()
                                    }
                                }
                                "React" -> {
                                    if (isNeedModel) {
                                        val params = HashMap<String, String>()
                                        params["conversionType"] = "jsonSchema"
                                        params["targetLanguage"] = "TypeScript"
                                        params["className"] = modelName
                                        params["jsonString"] = JSON.toJSONString(jsonSchema)
                                        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
                                            .getInterfaceModel(params)
                                            .subscribeOn(Schedulers.io())
                                            .subscribe {
                                                ReactWriteCommandBuilder()
                                                    .newBuilder(project)
                                                    .setPsiDirectory(directory)
                                                    .setInterfaceDetailInfo(interfaceDetail)
                                                    .setModelName(modelName)
                                                    .setInterfaceResponse(it)
                                                    .build()
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }
}
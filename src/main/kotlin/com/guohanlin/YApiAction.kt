package com.guohanlin

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.guohanlin.model.InterfaceDetailInfoDTO
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.ui.SelectApiDialog
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
            val modelName = dialog.modelInput.text.toString()
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
            val baseUri = SharePreferences.get(Constant.yApiBaseUri, Constant.BASE_URL)
            Api.getService(ApiService::class.java, baseUri).getInterfaceDetail(params)
                .subscribeOn(Schedulers.io())
                .subscribe { interfaceDetail ->
                    run {
                        if (interfaceDetail.errcode == 0) {
                            generateCode(interfaceDetail, selectPlatform, modelName, project, directory)
                        }
                    }
                }
        }
    }

    /**
     * 注释：生成对应语言代码
     * 时间：2022/3/26 11:00
     * 作者：郭翰林
     */
    private fun generateCode(
        interfaceDetail: InterfaceDetailInfoDTO,
        selectPlatform: String,
        modelName: String,
        project: Project,
        directory: PsiDirectory
    ) {
        val resBody: JSONObject = JSON.parseObject(interfaceDetail.data.res_body)
        val properties: JSONObject = resBody["properties"] as JSONObject
        val jsonSchema = properties["info"] as JSONObject
        //接口定义返回数据是不是数组
        val isArrayModel = jsonSchema["type"] == "array"
        when (selectPlatform) {
            "Java" -> {
                generateJavaCode(modelName, jsonSchema, project, directory, interfaceDetail, isArrayModel)
            }
            "Kotlin" -> {
                generateKotlinCode(modelName, jsonSchema, project, directory, interfaceDetail, isArrayModel)
            }
            "Dart" -> {
                generateDartCode(modelName, jsonSchema, project, directory, interfaceDetail, isArrayModel)
            }
            "TypeScript" -> {
                generateTsCode(modelName, jsonSchema, project, directory, interfaceDetail)
            }
            "C++" -> {
                generateCppCode(modelName, jsonSchema, project, directory, interfaceDetail)
            }
            "Swift" -> {
                generateSwiftCode(modelName, jsonSchema, project, directory, interfaceDetail)
            }
            "Go" -> {
                generateGoCode(modelName, jsonSchema, project, directory, interfaceDetail)
            }
            "Objective-C" -> {
                generateOcCode(modelName, jsonSchema, project, directory, interfaceDetail)
            }
        }
    }

    private fun generateOcCode(
        modelName: String,
        jsonSchema: JSONObject,
        project: Project,
        directory: PsiDirectory,
        interfaceDetail: InterfaceDetailInfoDTO
    ) {
        val params = HashMap<String, String>()
        params["conversionType"] = "jsonSchema"
        params["targetLanguage"] = "Objective-C"
        params["className"] = modelName
        params["jsonString"] = JSON.toJSONString(jsonSchema)
        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                OcWriteCommandBuilder()
                    .newBuilder(project)
                    .setPsiDirectory(directory)
                    .setInterfaceDetailInfo(interfaceDetail)
                    .setModelName(modelName)
                    .setInterfaceResponse(it)
                    .build()
            }
    }

    private fun generateGoCode(
        modelName: String,
        jsonSchema: JSONObject,
        project: Project,
        directory: PsiDirectory,
        interfaceDetail: InterfaceDetailInfoDTO
    ) {
        val params = HashMap<String, String>()
        params["conversionType"] = "jsonSchema"
        params["targetLanguage"] = "Go"
        params["className"] = modelName
        params["jsonString"] = JSON.toJSONString(jsonSchema)
        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                GoWriteCommandBuilder()
                    .newBuilder(project)
                    .setPsiDirectory(directory)
                    .setInterfaceDetailInfo(interfaceDetail)
                    .setModelName(modelName)
                    .setInterfaceResponse(it)
                    .build()
            }
    }

    private fun generateSwiftCode(
        modelName: String,
        jsonSchema: JSONObject,
        project: Project,
        directory: PsiDirectory,
        interfaceDetail: InterfaceDetailInfoDTO
    ) {
        val params = HashMap<String, String>()
        params["conversionType"] = "jsonSchema"
        params["targetLanguage"] = "Swift"
        params["className"] = modelName
        params["jsonString"] = JSON.toJSONString(jsonSchema)
        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                SwiftWriteCommandBuilder()
                    .newBuilder(project)
                    .setPsiDirectory(directory)
                    .setInterfaceDetailInfo(interfaceDetail)
                    .setModelName(modelName)
                    .setInterfaceResponse(it)
                    .build()
            }
    }

    private fun generateCppCode(
        modelName: String,
        jsonSchema: JSONObject,
        project: Project,
        directory: PsiDirectory,
        interfaceDetail: InterfaceDetailInfoDTO
    ) {
        val params = HashMap<String, String>()
        params["conversionType"] = "jsonSchema"
        params["targetLanguage"] = "C++"
        params["className"] = modelName
        params["jsonString"] = JSON.toJSONString(jsonSchema)
        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                CppWriteCommandBuilder()
                    .newBuilder(project)
                    .setPsiDirectory(directory)
                    .setInterfaceDetailInfo(interfaceDetail)
                    .setModelName(modelName)
                    .setInterfaceResponse(it)
                    .build()
            }
    }

    private fun generateTsCode(
        modelName: String,
        jsonSchema: JSONObject,
        project: Project,
        directory: PsiDirectory,
        interfaceDetail: InterfaceDetailInfoDTO
    ) {
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

    private fun generateDartCode(
        modelName: String,
        jsonSchema: JSONObject,
        project: Project,
        directory: PsiDirectory,
        interfaceDetail: InterfaceDetailInfoDTO,
        isArrayModel: Boolean
    ) {
        val params = HashMap<String, String>()
        params["conversionType"] = "jsonSchema"
        params["targetLanguage"] = "Dart"
        params["className"] = modelName
        params["jsonString"] = JSON.toJSONString(jsonSchema)
        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                DartWriteCommandBuilder()
                    .newBuilder(project)
                    .setPsiDirectory(directory)
                    .setInterfaceDetailInfo(interfaceDetail)
                    .setModelName(modelName)
                    .setInterfaceResponse(it)
                    .setIsArrayModel(isArrayModel)
                    .build()
            }
    }

    private fun generateKotlinCode(
        modelName: String,
        jsonSchema: JSONObject,
        project: Project,
        directory: PsiDirectory,
        interfaceDetail: InterfaceDetailInfoDTO,
        isArrayModel: Boolean
    ) {
        val params = HashMap<String, String>()
        params["conversionType"] = "jsonSchema"
        params["targetLanguage"] = "Kotlin"
        params["className"] = modelName
        params["jsonString"] = JSON.toJSONString(jsonSchema)
        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                KotlinWriteCommandBuilder()
                    .newBuilder(project)
                    .setPsiDirectory(directory)
                    .setInterfaceDetailInfo(interfaceDetail)
                    .setInterfaceResponse(it)
                    .setModelName(modelName)
                    .setIsArrayModel(isArrayModel)
                    .build()
            }
    }

    private fun generateJavaCode(
        modelName: String,
        jsonSchema: JSONObject,
        project: Project,
        directory: PsiDirectory,
        interfaceDetail: InterfaceDetailInfoDTO,
        isArrayModel: Boolean
    ) {
        val params = HashMap<String, String>()
        params["conversionType"] = "jsonSchema"
        params["targetLanguage"] = "Java"
        params["className"] = modelName
        params["jsonString"] = JSON.toJSONString(jsonSchema)
        Api.getService(ApiService::class.java, Constant.QUICK_TYPE_URL)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                JavaWriteCommandBuilder()
                    .newBuilder(project)
                    .setPsiDirectory(directory)
                    .setInterfaceDetailInfo(interfaceDetail)
                    .setInterfaceResponse(it)
                    .setModelName(modelName)
                    .setIsArrayModel(isArrayModel)
                    .build()
            }
    }
}
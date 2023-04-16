package com.guohanlin

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.guohanlin.builder.*
import com.guohanlin.json.CheckLicense
import com.guohanlin.model.InterfaceDetailInfoDTO
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.ui.Icons
import com.guohanlin.ui.SelectApiDialog
import com.guohanlin.utils.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.NlsActions
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import io.reactivex.schedulers.Schedulers
import java.lang.Boolean
import javax.swing.Icon
import kotlin.String
import kotlin.run


/**
 * 注释：YApi代码生成插件
 * 时间：2021/5/20 0020 20:15
 * 作者：郭翰林
 */
class YApiAction(
    @NlsActions.ActionText text: String? = message("action.yapi"),
    @NlsActions.ActionDescription description: String? = message("action.yapi"),
    icon: Icon? = Icons.yapiAction
) : AnAction(text, description, icon) {
    override fun actionPerformed(e: AnActionEvent) {
        //验证许可证明
        val isLicensed = Boolean.TRUE == CheckLicense.isLicensed()
        if (!isLicensed) {
            CheckLicense.requestLicense("Please register our plugin!")
            return
        }
        //获取插件环境
        val project = e.getData(PlatformDataKeys.PROJECT) ?: return
        val dialog = SelectApiDialog(project)
        if (dialog.showAndGet()) {
            //根据拿到的接口信息生成代码
            val interfaceInfo = dialog.interfaceInfo
            val projectSetting = dialog.projectSetting
            val selectPlatform = dialog.selectPlatform
            val modelName = dialog.modelInput.text.toString()
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
            val baseUri = SharePreferences.get(Constant.YAPI_BASE_URI, Constant.BASE_URL)
            Api.getService(ApiService::class.java, baseUri).getInterfaceDetail(params)
                .subscribeOn(Schedulers.io())
                .doOnError {
                    MyNotifier.notifyError(
                        project,
                        "${message("notify.getInterfaceDetail.error")}${it}"
                    )
                }
                .subscribe { interfaceDetail ->
                    run {
                        if (interfaceDetail.errcode == 0) {
                            requestQuickType(
                                interfaceDetail,
                                selectPlatform,
                                modelName,
                                project,
                                directory
                            )
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
    private fun requestQuickType(
        interfaceDetail: InterfaceDetailInfoDTO,
        selectPlatform: String,
        modelName: String,
        project: Project,
        directory: PsiDirectory
    ) {
        val resBody: JSONObject = JSON.parseObject(interfaceDetail.data.res_body)
        val properties: JSONObject = resBody["properties"] as JSONObject
        val needParseField = SharePreferences.get(Constant.NEED_PARSE_FIELD, "")
        var jsonString = ""
        //如果设置中配置了一级解析字段，则从实体中返回的配置的一级字段开始解析
        if (!StringUtils.isEmpty(needParseField)) {
            if (properties[needParseField] != null) {
                val jsonSchema = properties[needParseField] as JSONObject
                jsonString = JSON.toJSONString(jsonSchema)
            } else {
                MyNotifier.notifyMessage(project, message("setting.api.parseError"))
                jsonString = JSON.toJSONString(resBody)
            }
        } else {
            jsonString = JSON.toJSONString(resBody)
        }
        val params = HashMap<String, String>()
        params["conversionType"] = "jsonSchema"
        params["targetLanguage"] = selectPlatform
        params["className"] = modelName
        params["jsonString"] = jsonString
        MyNotifier.notifyMessage(project, message("notify.quickNode.loading"))
        //QuickTypeNode服务请求地址
        val quickTypeService =
            SharePreferences.get(Constant.QUICK_TYPE_SERVICE, Constant.QUICK_TYPE_URL)
        Api.getService(ApiService::class.java, quickTypeService)
            .getInterfaceModel(params)
            .subscribeOn(Schedulers.io())
            .doOnError {
                MyNotifier.notifyError(project, "${message("notify.quickNode.error")}${it}")
            }
            .subscribe {
                when (selectPlatform) {
                    "Java" -> {
                        JavaWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceDetailInfo(interfaceDetail)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "Kotlin" -> {
                        KotlinWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceDetailInfo(interfaceDetail)
                            .setInterfaceResponse(it)
                            .setModelName(modelName)
                            .build()
                    }
                    "Dart" -> {
                        DartWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceDetailInfo(interfaceDetail)
                            .setModelName(modelName)
                            .setInterfaceResponse(it)
                            .build()
                    }
                    "TypeScript" -> {
                        ReactWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceDetailInfo(interfaceDetail)
                            .setModelName(modelName)
                            .setInterfaceResponse(it)
                            .build()
                    }
                    "C++" -> {
                        CppWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceDetailInfo(interfaceDetail)
                            .setModelName(modelName)
                            .setInterfaceResponse(it)
                            .build()
                    }
                    "Swift" -> {
                        SwiftWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceDetailInfo(interfaceDetail)
                            .setModelName(modelName)
                            .setInterfaceResponse(it)
                            .build()
                    }
                    "Go" -> {
                        GoWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceDetailInfo(interfaceDetail)
                            .setModelName(modelName)
                            .setInterfaceResponse(it)
                            .build()
                    }
                    "Objective-C" -> {
                        OcWriteCommandBuilder()
                            .newBuilder(project)
                            .setPsiDirectory(directory)
                            .setInterfaceDetailInfo(interfaceDetail)
                            .setModelName(modelName)
                            .setInterfaceResponse(it)
                            .build()
                    }
                }
                MyNotifier.notifyMessage(project, message("notify.quickNode.success"))
            }
    }
}
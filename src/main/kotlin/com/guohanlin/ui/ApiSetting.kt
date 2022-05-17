package com.guohanlin.ui

import com.alibaba.fastjson.JSON
import com.guohanlin.*
import com.guohanlin.model.CatMenuData
import com.guohanlin.model.InterfaceInfo
import com.guohanlin.model.ProjectSetting
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.utils.MyNotifier
import com.guohanlin.utils.SharePreferences
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.ToolbarDecorator
import com.intellij.util.ui.JBDimension
import io.reactivex.schedulers.Schedulers
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.JTextField

class ApiSetting(private val project: Project) : JPanel(BorderLayout()) {
    private lateinit var apiBaseInput: JTextField
    private var projectSettingJson: String = ""
    private lateinit var table: ProjectSettingTable

    init {
        jScrollPanel(JBDimension(500, 300)) {
            jVerticalLinearLayout {
                jHorizontalLinearLayout {
                    jLabel("Api请求根路径")
                    fillSpace()
                }
                jLine()
                jHorizontalLinearLayout {
                    apiBaseInput =
                        jTextInput(
                            initText = SharePreferences.get(
                                Constant.yApiBaseUri,
                                Constant.BASE_URL
                            )
                        ) {
                            minimumSize = Dimension(350, 50)
                        }
                }
                jHorizontalLinearLayout {
                    jLabel("如果请求QuickTypeNode服务失败，请配置以下Host:172.67.196.35 quicktype.guohanlin.com  ")
                    jLink("点击复制") {
                        CopyPasteManager.getInstance()
                            .setContents(StringSelection("172.67.196.35 quicktype.guohanlin.com"))
                    }
                    fillSpace()
                }
                jLine()
                jHorizontalLinearLayout {
                    jLabel("项目配置")
                    fillSpace()
                }
                checkAddView(this, createContainer())
            }
        }
    }

    /**
     * 注释：创建容器Panel
     * 时间：2021/5/21 0021 9:19
     * 作者：郭翰林
     */
    private fun createContainer(): JPanel {
        table = if (!PropertiesComponent.getInstance().getValue(Constant.PROJECT_SETTING_CONFIG)
                .isNullOrEmpty()
        ) {
            projectSettingJson =
                PropertiesComponent.getInstance().getValue(Constant.PROJECT_SETTING_CONFIG)
                    .toString()
            val settingConfig = JSON.parseArray(projectSettingJson, ProjectSetting::class.java)
            ProjectSettingTable(settingConfig as ArrayList<ProjectSetting>)
        } else {
            val projectSetting = ProjectSetting(
                "测试项目",
                "143242",
                "f1b38e99a5b09073635ab6a901dc2af841f0f507db87086678838c237d9d165b"
            )
            val arrayList = arrayListOf<ProjectSetting>()
            arrayList.add(projectSetting)
            PropertiesComponent.getInstance()
                .setValue(
                    Constant.PROJECT_SETTING_CONFIG, JSON.toJSONString(arrayList)
                )
            ProjectSettingTable(arrayList)
        }
        //设置双击事件绑定
        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                table.editProjectSetting()
                return true
            }
        }.installOn(table)

        return ToolbarDecorator
            .createDecorator(table)
            .setAddAction { table.addProjectSetting() }
            .setRemoveAction { table.removeProjectSetting() }
            .setEditAction { table.editProjectSetting() }
            .createPanel()
    }

    /**
     * 注释：是否有更改
     * 时间：2021/7/10 0010 15:43
     * 作者：郭翰林
     */
    fun isModified(): Boolean {
        val items = table.getItems()
        val currentData = JSON.toJSONString(items)
        val baseUri = SharePreferences.get(Constant.yApiBaseUri, Constant.BASE_URL)
        return currentData != projectSettingJson || baseUri != apiBaseInput.text
    }

    /**
     * 注释：重置
     * 时间：2021/7/10 0010 15:44
     * 作者：郭翰林
     */
    fun reset() {
        table.reset()
        val baseUri = SharePreferences.get(Constant.yApiBaseUri, Constant.BASE_URL)
        apiBaseInput.text = baseUri
    }

    /**
     * 注释：应用更改
     * 时间：2021/7/10 0010 15:45
     * 作者：郭翰林
     */
    fun apply() {
        val items = table.getItems()
        //注入内存和缓存
        Constant.projectList = items as ArrayList<ProjectSetting>
        PropertiesComponent.getInstance()
            .setValue(Constant.PROJECT_SETTING_CONFIG, JSON.toJSONString(items))
        SharePreferences.put(Constant.yApiBaseUri, apiBaseInput.text)
        initYApiProjectSetting(project, settingConfig = items)
    }

    /**
     * 注释：初始化YApi工程配置
     * 时间：2021/7/10 0010 16:18
     * 作者：郭翰林
     */
    private fun initYApiProjectSetting(project: Project, settingConfig: List<ProjectSetting>) {
        val params = HashMap<String, String>()
        params["project_id"] = settingConfig[0].projectId
        params["token"] = settingConfig[0].projectToken
        val token: String = settingConfig[0].projectToken
        val baseUri = SharePreferences.get(Constant.yApiBaseUri, Constant.BASE_URL)
        //请求第一个工程的YApi接口菜单
        Api.getService(ApiService::class.java, baseUri).getCatMenu(params)
            .subscribeOn(Schedulers.io())
            .doOnError {
                MyNotifier.notifyError(project, "获取分类菜单接口请求失败，原因：${it}")
            }
            .subscribe {
                //注入到内存当中
                if (it.errcode == 0) {
                    Constant.catMenuDataList = it.data as ArrayList<CatMenuData>
                    //请求第一个分类下的接口数据
                    val catParams = HashMap<String, String>()
                    catParams["catid"] = it.data[0]._id.toString()
                    catParams["token"] = token
                    Api.getService(ApiService::class.java, baseUri).getInterfaceByCat(catParams)
                        .subscribeOn(Schedulers.io())
                        .doOnError {
                            MyNotifier.notifyError(project, "获取某个分类下的接口列表接口失败，原因：${it}")
                        }
                        .subscribe { it ->
                            if (it.errcode == 0 && it.data.count > 0) {
                                Constant.interfaceList = it.data.list as ArrayList<InterfaceInfo>
                            }
                        }
                }
            }
    }
}
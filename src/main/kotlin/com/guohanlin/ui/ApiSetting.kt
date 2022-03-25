package com.guohanlin.ui

import com.alibaba.fastjson.JSON
import com.guohanlin.*
import com.guohanlin.model.ProjectSetting
import com.guohanlin.utils.SharePreferences
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.ToolbarDecorator
import com.intellij.util.ui.JBDimension
import java.awt.BorderLayout
import java.awt.Dimension
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
                    jLabel("Api请求根路径(配置完，需重启才能生效)")
                    fillSpace()
                }
                jLine()
                jHorizontalLinearLayout {
                    apiBaseInput =
                        jTextInput(initText = SharePreferences.get(Constant.yApiBaseUri, Constant.BASE_URL)) {
                            minimumSize = Dimension(350, 50)
                        }
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
    }
}
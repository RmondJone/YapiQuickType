package com.guohanlin

import com.alibaba.fastjson.JSON
import com.guohanlin.model.ProjectSetting
import com.guohanlin.ui.ApiSetting
import com.guohanlin.ui.ProjectSettingTable
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBTabbedPane
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * 注释：新康众YApi代码生成插件项目配置
 * 时间：2021/5/21 0021 8:57
 * 作者：郭翰林
 */
class ProjectSettingConfig(private val project: Project) : SearchableConfigurable {
    private var projectSettingJson: String = ""
    private lateinit var table: ProjectSettingTable
    private lateinit var apiSetting: ApiSetting

    override fun createComponent(): JComponent {
        apiSetting = ApiSetting(project)
        return JBTabbedPane().apply {
            add("YApi设置", apiSetting)
            add("YApi项目配置", createContainer())
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
                "康众汽配",
                "22",
                "bbb082219266b9a54c3e925636867b8cfaec5d39b0c44c2e5cbfd3e35d026592"
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

    override fun isModified(): Boolean {
        val items = table.getItems()
        val currentData = JSON.toJSONString(items)
        val androidTempChange = apiSetting.isModified()
        return currentData != projectSettingJson || androidTempChange
    }

    override fun reset() {
        table.reset()
        apiSetting.reset()
    }

    override fun apply() {
        val items = table.getItems()
        apiSetting.apply()
        //注入内存和缓存
        Constant.projectList = items as ArrayList<ProjectSetting>
        PropertiesComponent.getInstance()
            .setValue(Constant.PROJECT_SETTING_CONFIG, JSON.toJSONString(items))
    }

    override fun getDisplayName(): String {
        return "YApi代码生成插件"
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun getId(): String {
        return "plugins.guohanlin.api"
    }

}
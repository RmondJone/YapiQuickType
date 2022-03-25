package com.guohanlin

import com.guohanlin.ui.ApiSetting
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTabbedPane
import javax.swing.JComponent

/**
 * 注释：新康众YApi代码生成插件项目配置
 * 时间：2021/5/21 0021 8:57
 * 作者：郭翰林
 */
class ProjectSettingConfig(private val project: Project) : SearchableConfigurable {
    private lateinit var apiSetting: ApiSetting

    override fun createComponent(): JComponent {
        apiSetting = ApiSetting(project)
        return JBTabbedPane().apply {
            add("YApi设置", apiSetting)
        }
    }


    override fun isModified(): Boolean {
        return apiSetting.isModified()
    }

    override fun reset() {
        apiSetting.reset()
    }

    override fun apply() {
        apiSetting.apply()
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
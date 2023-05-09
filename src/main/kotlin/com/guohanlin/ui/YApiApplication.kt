package com.guohanlin.ui

import com.alibaba.fastjson.JSON
import com.guohanlin.Constant
import com.guohanlin.model.ProjectSetting
import com.guohanlin.utils.updateYApiProjectSetting
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * 注释：插件启动类
 * 时间：2021/5/24 0024 9:23
 * 作者：郭翰林
 */
class YApiApplication : StartupActivity, DumbAware {
    override fun runActivity(project: Project) {
        //初始化YApi工程配置
        initYApiProjectSetting(project)
    }

    /**
     * 注释：初始化YApi工程配置
     * 时间：2021/7/10 0010 16:18
     * 作者：郭翰林
     */
    private fun initYApiProjectSetting(project: Project) {
        val settingConfig =
            PropertiesComponent.getInstance().getValue(Constant.YApiProjectSetting)
        if (settingConfig.isNullOrEmpty()) {
            val projectSetting = ProjectSetting(
                "Test Project",
                "11",
                "2c18d9128e8d0b627d2a8c7e48c7e9f8777e300025e7e4206865a2a282308cfb"
            )
            Constant.projectList = arrayListOf(projectSetting)
        } else {
            val jsonArray = JSON.parseArray(settingConfig, ProjectSetting::class.java)
            Constant.projectList = jsonArray as ArrayList<ProjectSetting>
        }
        updateYApiProjectSetting(project, Constant.projectList[0])
    }
}
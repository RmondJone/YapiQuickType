package com.guohanlin

import com.alibaba.fastjson.JSON
import com.guohanlin.model.CatMenuData
import com.guohanlin.model.InterfaceInfo
import com.guohanlin.model.ProjectSetting
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import io.reactivex.schedulers.Schedulers

/**
 * 注释：插件启动类
 * 时间：2021/5/24 0024 9:23
 * 作者：郭翰林
 */
class YApiApplication : StartupActivity, DumbAware {
    override fun runActivity(project: Project) {
        //初始化YApi工程配置
        initYApiProjectSetting()
    }

    /**
     * 注释：初始化YApi工程配置
     * 时间：2021/7/10 0010 16:18
     * 作者：郭翰林
     */
    private fun initYApiProjectSetting() {
        val settingConfig =
            PropertiesComponent.getInstance().getValue(Constant.PROJECT_SETTING_CONFIG)
        val token: String
        val params = HashMap<String, String>()
        if (settingConfig.isNullOrEmpty()) {
            params["project_id"] = "22"
            params["token"] = "bbb082219266b9a54c3e925636867b8cfaec5d39b0c44c2e5cbfd3e35d026592"
            token = "bbb082219266b9a54c3e925636867b8cfaec5d39b0c44c2e5cbfd3e35d026592"
            val projectSetting = ProjectSetting(
                "康众汽配",
                "22",
                "bbb082219266b9a54c3e925636867b8cfaec5d39b0c44c2e5cbfd3e35d026592"
            )
            Constant.projectList = arrayListOf(projectSetting)
        } else {
            val jsonArray = JSON.parseArray(settingConfig, ProjectSetting::class.java)
            params["project_id"] = jsonArray[0].projectId
            params["token"] = jsonArray[0].projectToken
            token = jsonArray[0].projectToken
            Constant.projectList = jsonArray as ArrayList<ProjectSetting>
        }
        //请求第一个工程的YApi接口菜单
        Api.getService(ApiService::class.java, Constant.BASE_URL).getCatMenu(params)
            .subscribeOn(Schedulers.io())
            .subscribe {
                //注入到内存当中
                if (it.errcode == 0) {
                    Constant.catMenuDataList = it.data as ArrayList<CatMenuData>
                    //请求第一个分类下的接口数据
                    val catParams = HashMap<String, String>()
                    catParams["catid"] = it.data[0]._id.toString()
                    catParams["token"] = token
                    Api.getService(ApiService::class.java, Constant.BASE_URL).getInterfaceByCat(catParams)
                        .subscribeOn(Schedulers.io())
                        .subscribe {
                            if (it.errcode == 0 && it.data.count > 0) {
                                Constant.interfaceList = it.data.list as ArrayList<InterfaceInfo>
                            }
                        }
                }
            }
    }
}
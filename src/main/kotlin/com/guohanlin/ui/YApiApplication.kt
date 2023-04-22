package com.guohanlin.ui

import com.alibaba.fastjson.JSON
import com.guohanlin.Constant
import com.guohanlin.model.CatMenuData
import com.guohanlin.model.InterfaceInfo
import com.guohanlin.model.ProjectSetting
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.utils.MyNotifier
import com.guohanlin.utils.SharePreferences
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
        val token: String
        val params = HashMap<String, String>()
        if (settingConfig.isNullOrEmpty()) {
            params["project_id"] = "11"
            params["token"] = "2c18d9128e8d0b627d2a8c7e48c7e9f8777e300025e7e4206865a2a282308cfb"
            token = "2c18d9128e8d0b627d2a8c7e48c7e9f8777e300025e7e4206865a2a282308cfb"
            val projectSetting = ProjectSetting(
                "Test Project",
                "11",
                "2c18d9128e8d0b627d2a8c7e48c7e9f8777e300025e7e4206865a2a282308cfb"
            )
            Constant.projectList = arrayListOf(projectSetting)
        } else {
            val jsonArray = JSON.parseArray(settingConfig, ProjectSetting::class.java)
            params["project_id"] = jsonArray[0].projectId
            params["token"] = jsonArray[0].projectToken
            token = jsonArray[0].projectToken
            Constant.projectList = jsonArray as ArrayList<ProjectSetting>
        }
        val baseUri = SharePreferences.get(Constant.YApiBaseUri, Constant.BASE_URL)
        println("YApi路径：$baseUri")
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
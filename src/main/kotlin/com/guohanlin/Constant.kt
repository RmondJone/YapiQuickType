package com.guohanlin

import com.guohanlin.model.CatMenuData
import com.guohanlin.model.InterfaceInfo
import com.guohanlin.model.ProjectSetting

object Constant {
    //项目配置缓存
    const val PROJECT_SETTING_CONFIG = "project_setting_config"

    //基础Uri
    const val BASE_URL = "http://yapi.smart-xwork.cn/"

    //QuickType服务地址
    const val QUICK_TYPE_URL = "http://quicktype.guohanlin.com/"

    //基本路径保存Key
    const val yApiBaseUri = "yApiBaseUri"

    //项目配置列表
    var projectList: ArrayList<ProjectSetting> = arrayListOf()

    //YApi分类菜单
    var catMenuDataList: ArrayList<CatMenuData> = arrayListOf()

    //当前分类下所有接口集合
    var interfaceList: ArrayList<InterfaceInfo> = arrayListOf()

    //生成语言集合
    var platformList: ArrayList<String> =
        arrayListOf<String>("Java", "Kotlin", "Dart", "TypeScript", "C++", "Swift", "Objective-C", "Go")
}
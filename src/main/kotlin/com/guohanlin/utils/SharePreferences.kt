package com.guohanlin.utils

import com.guohanlin.Constant
import com.intellij.ide.util.PropertiesComponent

/**
 * 注释：存储保存工具
 * 时间：2021/7/10 0010 15:58
 * 作者：郭翰林
 */
object SharePreferences {
    /**
     * 注释：存储
     * 时间：2021/7/10 0010 16:03
     * 作者：郭翰林
     */
    @JvmStatic
    fun <T> put(key: String, value: T) {
        when (value) {
            is String -> PropertiesComponent.getInstance().setValue(key, value)
            is Boolean -> PropertiesComponent.getInstance().setValue(key, value)
            is Float -> PropertiesComponent.getInstance().setValue(key, value, 0f)
            is Int -> PropertiesComponent.getInstance().setValue(key, value, 0)
        }
    }

    /**
     * 注释：获取
     * 时间：2021/7/10 0010 16:12
     * 作者：郭翰林
     */
    @JvmStatic
    fun <T> get(key: String, default: T): T {
        return when (default) {
            is String -> (PropertiesComponent.getInstance().getValue(key) ?: default) as T
            is Boolean -> PropertiesComponent.getInstance().getBoolean(key) as T
            is Long -> PropertiesComponent.getInstance().getLong(key, default) as T
            is Float -> PropertiesComponent.getInstance().getFloat(key, default) as T
            is Int -> PropertiesComponent.getInstance().getInt(key, default) as T
            else -> default
        }
    }

    /**
     * 注释：删除
     * 时间：2023/4/18 15:56
     * 作者：郭翰林
     */
    @JvmStatic
    fun remove(key: String) {
        PropertiesComponent.getInstance().unsetValue(key)
    }

    /**
     * 注释：移除所有
     * 时间：2023/4/18 16:10
     * 作者：郭翰林
     */
    fun removeAll() {
        PropertiesComponent.getInstance().unsetValue(Constant.YApiBaseUri)
        PropertiesComponent.getInstance().unsetValue(Constant.NeedParseField)
        PropertiesComponent.getInstance().unsetValue(Constant.QuickTypeService)
        PropertiesComponent.getInstance().unsetValue(Constant.YApiProjectSetting)
    }
}
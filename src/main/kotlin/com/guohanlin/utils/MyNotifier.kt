package com.guohanlin.utils

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import java.util.*
import kotlin.concurrent.schedule

/**
 * 注释：通知管理器
 * 时间：2022/4/12 5:46 下午
 * 作者：郭翰林
 */
object MyNotifier {
    /**
     * 注释：错误悬浮通知
     * 时间：2022/4/12 5:46 下午
     * 作者：郭翰林
     */
    @JvmStatic
    fun notifyError(project: Project, content: String) {
        if (getIDEVersion() > 203) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("YApi QuickType Error")
                .createNotification(content, NotificationType.ERROR)
                .notify(project)
        } else {
            //2020.3版本以下使用这种方式通知
            NotificationGroup("YApi QuickType Error", NotificationDisplayType.BALLOON, true)
                .createNotification(content, NotificationType.ERROR)
                .notify(project)
        }
    }

    /**
     * 注释：信息悬浮通知
     * 时间：2022/4/12 7:22 下午
     * 作者：郭翰林
     */
    @JvmStatic
    fun notifyMessage(project: Project, content: String) {
        val notification = if (getIDEVersion() > 203) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("YApi QuickType Message")
                .createNotification(content, NotificationType.INFORMATION)
        } else {
            //2020.3版本以下使用这种方式通知
            NotificationGroup("YApi QuickType Message", NotificationDisplayType.BALLOON, true)
                .createNotification(content, NotificationType.INFORMATION)
        }
        notification.notify(project)
        //2s之后消失
        Timer().schedule(2000) {
            notification.expire()
        }
    }

    /**
     * 注释：获取IDE版本号
     * 时间：2022/4/13 6:33 下午
     * 作者：郭翰林
     */
    private fun getIDEVersion(): Int {
        return ApplicationInfo.getInstance().build.components[0]
    }
}
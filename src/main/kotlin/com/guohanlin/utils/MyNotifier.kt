package com.guohanlin.utils

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

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
        NotificationGroupManager.getInstance()
            .getNotificationGroup("YApi QuickType Error")
            .createNotification(content, NotificationType.ERROR)
            .notify(project);
    }
}
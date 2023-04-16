package com.guohanlin.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.NlsActions
import com.intellij.openapi.util.NlsActions.ActionText
import javax.swing.Icon

/**
 * 注释：设置按钮
 * 时间：2022/5/17 11:09 上午
 * 作者：郭翰林
 */
class SettingsAction(
    @ActionText text: String? = "YApi设置",
    @NlsActions.ActionDescription description: String? = "YApi设置",
    icon: Icon? = AllIcons.General.GearPlain
) : AnAction(text, description, icon), DumbAware {

    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance()
            .showSettingsDialog(e.project, ProjectSettingConfig::class.java)
    }

}
package com.guohanlin.ui

import com.guohanlin.utils.*
import com.guohanlin.model.ProjectSetting
import com.intellij.openapi.ui.DialogWrapper
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JTextField

/**
 * 注释：项目编辑框
 * 时间：2021/5/21 0021 11:07
 * 作者：郭翰林
 */
class ProjectSettingEditor(canBeParent: Boolean, projectSetting: ProjectSetting) :
    DialogWrapper(canBeParent) {
    private var projectSetting = projectSetting

    private lateinit var projectNameInput: JTextField
    private lateinit var projectIdInput: JTextField
    private lateinit var projectTokenInput: JTextField

    init {
        init()
        title = message("project.setting.projectEditText")
    }

    override fun createCenterPanel(): JComponent? {
        return jVerticalLinearLayout {
            jHorizontalLinearLayout {
                jLabel(message("project.setting.projectName")) { preferredSize = Dimension(100, 20) }
                projectNameInput = jTextInput(initText = projectSetting.projectName) {
                    minimumSize = Dimension(350, 45)
                }
            }
            jHorizontalLinearLayout {
                jLabel(message("project.setting.projectId")) { preferredSize = Dimension(100, 20) }
                projectIdInput = jTextInput(initText = projectSetting.projectId) {
                    minimumSize = Dimension(350, 45)
                }
            }
            jHorizontalLinearLayout {
                jLabel(message("project.setting.projectToken")) { preferredSize = Dimension(100, 20) }
                projectTokenInput = jTextInput(initText = projectSetting.projectToken) {
                    minimumSize = Dimension(350, 45)
                }
            }
        }
    }

    fun getProjectSetting(): ProjectSetting {
        projectSetting.projectName = projectNameInput.text.toString()
        projectSetting.projectId = projectIdInput.text.toString()
        projectSetting.projectToken = projectTokenInput.text.toString()
        return projectSetting
    }
}
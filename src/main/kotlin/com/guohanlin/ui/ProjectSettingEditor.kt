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
        title = "项目编辑框"
    }

    override fun createCenterPanel(): JComponent? {
        return jVerticalLinearLayout {
            jHorizontalLinearLayout {
                jLabel("项目名称") { preferredSize = Dimension(80, 20) }
                projectNameInput = jTextInput(initText = projectSetting.projectName) {
                    minimumSize = Dimension(350, 50)
                }
            }
            jHorizontalLinearLayout {
                jLabel("项目Id") { preferredSize = Dimension(80, 20) }
                projectIdInput = jTextInput(initText = projectSetting.projectId) {
                    minimumSize = Dimension(350, 50)
                }
            }
            jHorizontalLinearLayout {
                jLabel("项目Token") { preferredSize = Dimension(80, 20) }
                projectTokenInput = jTextInput(initText = projectSetting.projectToken) {
                    minimumSize = Dimension(350, 50)
                }
            }
            minimumSize = Dimension(450, 150)
        }
    }

    fun getProjectSetting(): ProjectSetting {
        projectSetting.projectName = projectNameInput.text.toString()
        projectSetting.projectId = projectIdInput.text.toString()
        projectSetting.projectToken = projectTokenInput.text.toString()
        return projectSetting
    }
}
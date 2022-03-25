package com.guohanlin.ui

import com.guohanlin.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.util.ui.JBDimension
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.JTextField

class ApiSetting(private val project: Project) : JPanel(BorderLayout()) {
    private lateinit var apiBaseInput: JTextField

    init {
        jScrollPanel(JBDimension(500, 300)) {
            jVerticalLinearLayout {
                jHorizontalLinearLayout {
                    jLabel("YApi Api请求根路径")
                    fillSpace()
                }
                apiBaseInput = jTextInput(initText = Constant.BASE_URL) {
                    minimumSize = Dimension(350, 50)
                }
            }
        }
    }

    /**
     * 注释：是否有更改
     * 时间：2021/7/10 0010 15:43
     * 作者：郭翰林
     */
    fun isModified(): Boolean {
        return false;
    }

    /**
     * 注释：重置
     * 时间：2021/7/10 0010 15:44
     * 作者：郭翰林
     */
    fun reset() {
        ApplicationManager.getApplication().runWriteAction(Runnable {

        })
    }

    /**
     * 注释：应用更改
     * 时间：2021/7/10 0010 15:45
     * 作者：郭翰林
     */
    fun apply() {
    }
}
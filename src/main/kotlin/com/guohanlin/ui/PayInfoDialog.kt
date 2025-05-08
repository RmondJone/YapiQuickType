package com.guohanlin.ui

import com.guohanlin.utils.jButton
import com.guohanlin.utils.jHorizontalLinearLayout
import com.guohanlin.utils.jImage
import com.guohanlin.utils.jLabel
import com.guohanlin.utils.jVerticalLinearLayout
import com.guohanlin.utils.message
import com.guohanlin.utils.showMessageTip
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.DialogWrapper
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import javax.swing.JComponent

/**
 * 注释：支付弹窗
 * 时间：2024/12/8 22:33
 * 作者：郭翰林
 */
class PayInfoDialog : DialogWrapper(ProjectManager.getInstance().defaultProject) {
    init {
        init()
        title = message("commit.panel.supportMe")
        setOKButtonText(message("commit.panel.supportThankYou"))
    }

    override fun createCenterPanel(): JComponent {
        return jVerticalLinearLayout {
            jHorizontalLinearLayout {
                jLabel(message("commit.panel.donating"))
                fillSpace()
            }
            fixedSpace(20)
            jHorizontalLinearLayout {
                jImage("/images/alipay.png")
                fixedSpace(30)
                jImage("/images/weixinpay.png")
                fillSpace()
            }
            fixedSpace(20)
            jHorizontalLinearLayout {
                jLabel(message("donateUSDT"))
                fillSpace()
            }
            jHorizontalLinearLayout {
                jLabel("Trc2.0: TVoRsS4yBRSXfZiHwtWXc2hK8vBAePNJ5y")
                fixedSpace(10)
                jButton(message("copy"), {
                    Toolkit.getDefaultToolkit().systemClipboard.setContents(
                        StringSelection("TVoRsS4yBRSXfZiHwtWXc2hK8vBAePNJ5y"),
                        null
                    )
                    showMessageTip(message("copySuccess"))
                })
                fillSpace()
            }
        }
    }

}
package com.guohanlin.ui

import com.guohanlin.*
import com.guohanlin.model.CatMenuData
import com.guohanlin.model.InterfaceInfo
import com.guohanlin.model.ProjectSetting
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.utils.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import io.reactivex.schedulers.Schedulers
import java.awt.Dimension
import java.awt.event.ItemEvent
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JTextField

/**
 * 注释：选择Api接口弹窗
 * 时间：2021/5/24 0024 11:48
 * 作者：郭翰林
 */
class SelectApiDialog(private val project: Project) : DialogWrapper(project) {
    private lateinit var projectJComboBox: JComboBox<Any>
    private lateinit var catMenuJComboBox: JComboBox<Any>
    private lateinit var interfaceJComboBox: JComboBox<Any>
    private lateinit var platformJComboBox: JComboBox<Any>

    lateinit var projectSetting: ProjectSetting
    lateinit var catMenuData: CatMenuData
    lateinit var interfaceInfo: InterfaceInfo
    lateinit var selectPlatform: String
    lateinit var modelInput: JTextField


    init {
        init()
        title = message("yapi.dialog.title")
        try {
            projectSetting = Constant.projectList[0]
            catMenuData = Constant.catMenuDataList[0]
            interfaceInfo = Constant.interfaceList[0]
            selectPlatform = Constant.platformList[0]
        } catch (e: Exception) {
            MyNotifier.notifyError(project, message("notify.pluginInit.error"))
        }
    }

    override fun doOKAction() {
        val modelName = modelInput.text.toString()
        if (StringUtils.isEmpty(modelName)) {
            showMessageTip(message("yapi.dialog.tip"))
            return
        }
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent? {
        return jVerticalLinearLayout {
            jHorizontalLinearLayout {
                jLabel(message("yapi.dialog.project"))
                projectJComboBox = jComboBox(items = Constant.projectList.toArray()) {
                    it?.let {
                        if (it.stateChange == ItemEvent.SELECTED) {
                            projectSetting = projectJComboBox.selectedItem as ProjectSetting
                            changeCatMenuJComboBox()
                        }
                    }
                }
            }
            jHorizontalLinearLayout {
                jLabel(message("yapi.dialog.catMenu"))
                catMenuJComboBox = jComboBox(items = Constant.catMenuDataList.toArray()) {
                    it?.let {
                        if (it.stateChange == ItemEvent.SELECTED) {
                            catMenuData = catMenuJComboBox.selectedItem as CatMenuData
                            changeInterfaceJComboBox()
                        }
                    }
                }
            }
            jHorizontalLinearLayout {
                jLabel(message("yapi.dialog.interfaceList"))
                interfaceJComboBox = jComboBox(items = Constant.interfaceList.toArray()) {
                    it?.let {
                        if (it.stateChange == ItemEvent.SELECTED) {
                            interfaceInfo = interfaceJComboBox.selectedItem as InterfaceInfo
                        }
                    }
                }
            }
            jHorizontalLinearLayout {
                jLabel(message("yapi.dialog.platformList"))
                platformJComboBox = jComboBox(items = Constant.platformList.toArray()) {
                    it?.let {
                        if (it.stateChange == ItemEvent.SELECTED) {
                            selectPlatform = platformJComboBox.selectedItem as String
                        }
                    }
                }
            }
            jHorizontalLinearLayout {
                jLabel(message("yapi.dialog.modelName"))
                modelInput = jTextInput {
                    minimumSize = Dimension(100, 40)
                    maximumSize = Dimension(300, 40)
                    document = NumberTextField(30)
                }
            }
            jHorizontalLinearLayout {
                fillSpace()
                jActionButton(SettingsAction())
                fixedSpace(25)
            }
        }
    }

    /**
     * 注释：改变接口分类列表
     * 时间：2021/5/26 0026 10:13
     * 作者：郭翰林
     */
    fun changeCatMenuJComboBox() {
        val params = HashMap<String, String>()
        params["token"] = projectSetting.projectToken
        params["project_id"] = projectSetting.projectId
        val baseUri = SharePreferences.get(Constant.YAPI_BASE_URI, Constant.BASE_URL)
        Api.getService(ApiService::class.java, baseUri).getCatMenu(params)
            .subscribeOn(Schedulers.io())
            .doOnError {
                MyNotifier.notifyError(project, "${message("notify.getCatMenu.error")}${it}")
            }
            .subscribe {
                if (it.errcode == 0) {
                    catMenuJComboBox.removeAllItems()
                    for (item in it.data) {
                        catMenuJComboBox.addItem(item)
                    }
                }
            }
    }

    /**
     * 注释：改变分类接口列表
     * 时间：2021/5/26 0026 10:14
     * 作者：郭翰林
     */
    fun changeInterfaceJComboBox() {
        val params = HashMap<String, String>()
        params["catid"] = catMenuData._id.toString()
        params["token"] = projectSetting.projectToken
        val baseUri = SharePreferences.get(Constant.YAPI_BASE_URI, Constant.BASE_URL)
        Api.getService(ApiService::class.java, baseUri).getInterfaceByCat(params)
            .subscribeOn(Schedulers.io())
            .doOnError {
                MyNotifier.notifyError(project, "${message("notify.getInterfaceList.error")}${it}")
            }
            .subscribe {
                if (it.errcode == 0) {
                    interfaceJComboBox.removeAllItems()
                    for (item in it.data.list) {
                        interfaceJComboBox.addItem(item)
                    }
                }
            }
    }
}
package com.guohanlin.ui

import com.guohanlin.*
import com.guohanlin.model.CatMenuData
import com.guohanlin.model.InterfaceInfo
import com.guohanlin.model.ProjectSetting
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.utils.NumberTextField
import com.guohanlin.utils.SharePreferences
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
class SelectApiDialog(canBeParent: Boolean) : DialogWrapper(canBeParent) {
    private lateinit var projectJComboBox: JComboBox<Any>
    private lateinit var catMenuJComboBox: JComboBox<Any>
    private lateinit var interfaceJComboBox: JComboBox<Any>
    private lateinit var platformJComboBox: JComboBox<Any>

    var projectSetting: ProjectSetting
    var catMenuData: CatMenuData
    var interfaceInfo: InterfaceInfo
    var selectPlatform: String
    lateinit var modelInput: JTextField

    init {
        init()
        title = "请选择需要生成代码的接口"
        projectSetting = Constant.projectList[0]
        catMenuData = Constant.catMenuDataList[0]
        interfaceInfo = Constant.interfaceList[0]
        selectPlatform = Constant.platformList[0]
    }

    override fun createCenterPanel(): JComponent? {
        return jVerticalLinearLayout {
            jHorizontalLinearLayout {
                jLabel("选择项目：")
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
                jLabel("接口分类：")
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
                jLabel("接口列表：")
                interfaceJComboBox = jComboBox(items = Constant.interfaceList.toArray()) {
                    it?.let {
                        if (it.stateChange == ItemEvent.SELECTED) {
                            interfaceInfo = interfaceJComboBox.selectedItem as InterfaceInfo
                        }
                    }
                }
            }
            jHorizontalLinearLayout {
                jLabel("生成语言：")
                platformJComboBox = jComboBox(items = Constant.platformList.toArray()) {
                    it?.let {
                        if (it.stateChange == ItemEvent.SELECTED) {
                            selectPlatform = platformJComboBox.selectedItem as String
                        }
                    }
                }
            }
            jHorizontalLinearLayout {
                jLabel("实体名(驼峰写法)：")
                modelInput = jTextInput {
                    minimumSize = Dimension(100, 50)
                    document = NumberTextField(30)
                }
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
        val baseUri = SharePreferences.get(Constant.yApiBaseUri, Constant.BASE_URL)
        Api.getService(ApiService::class.java, baseUri).getCatMenu(params)
            .subscribeOn(Schedulers.io())
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
        val baseUri = SharePreferences.get(Constant.yApiBaseUri, Constant.BASE_URL)
        Api.getService(ApiService::class.java, baseUri).getInterfaceByCat(params)
            .subscribeOn(Schedulers.io())
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
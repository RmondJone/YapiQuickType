package com.guohanlin.utils

import com.guohanlin.CodeStructure
import com.guohanlin.Constant
import com.guohanlin.model.CatMenuData
import com.guohanlin.model.InterfaceInfo
import com.guohanlin.model.ProjectSetting
import com.guohanlin.network.api.Api
import com.guohanlin.network.api.ApiService
import com.guohanlin.ui.PayInfoDialog
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.impl.file.PsiDirectoryFactory
import io.reactivex.schedulers.Schedulers

/**
 * 注释：创建Psi文件夹
 * 时间：2021/5/27 0027 17:07
 * 作者：郭翰林
 */
fun createPsiDirectory(directory: PsiDirectory, directoryName: String): PsiDirectory {
    return if (directory.findSubdirectory(directoryName) == null) {
        directory.createSubdirectory(directoryName)
    } else {
        directory.findSubdirectory(directoryName)!!
    }
}

/**
 * 注释：创建Psi文件
 * 时间：2021/5/27 0027 17:27
 * 作者：郭翰林
 */
fun creatPsiFile(directory: PsiDirectory, codeStructure: CodeStructure) {
    var psiFile = directory.findFile(codeStructure.creatFileName())
    if (psiFile != null) {
        //如果不为空则去更新代码
        codeStructure.updateCode(psiFile)
    } else {
        //如果为空，则新建
        psiFile = codeStructure.getFile()
        if (psiFile != null) {
            directory.add(psiFile)
        }
    }
    //显示捐赠弹窗
    ApplicationManager.getApplication().invokeLater(Runnable {
        val payInfoDialog: PayInfoDialog = PayInfoDialog()
        payInfoDialog.show()
    })
}


/**
 * 注释：获取包名
 * 时间：2021/5/27 0027 18:17
 * 作者：郭翰林
 */
fun getPackageName(directory: PsiDirectory): String {
    val directoryFactory = PsiDirectoryFactory.getInstance(directory.project)
    //当前包名
    return directoryFactory.getQualifiedName(directory, false)
}

/**
 * 注释：第一个字母大写其余字母全部小写
 * 时间：2020/5/27 0027 15:09
 * 作者：郭翰林
 *
 * @param text
 * @return
 */
fun captureName(text: String): String? {
    var text = text
    if (text.isNotEmpty()) {
        text = text.substring(0, 1).uppercase() + text.substring(1)
    }
    return text
}

/**
 * 注释：重复字符串多少次
 * 时间：2020/5/27 0027 14:07
 * 作者：郭翰林
 *
 * @param repeat
 * @param number
 * @return
 */
fun repeatStr(repeat: String?, number: Int): String? {
    val stringBuilder = StringBuilder()
    for (i in 0 until number) {
        stringBuilder.append(repeat)
    }
    return stringBuilder.toString()
}

/**
 * 注释：更新YApi工程配置
 * 时间：2021/7/10 0010 16:18
 * 作者：郭翰林
 */
fun updateYApiProjectSetting(
    project: Project,
    settingConfig: ProjectSetting,
) {
    val params = HashMap<String, String>()
    params["project_id"] = settingConfig.projectId
    params["token"] = settingConfig.projectToken
    val token: String = settingConfig.projectToken
    val baseUri = SharePreferences.get(Constant.YApiBaseUri, Constant.BASE_URL)
    //请求第一个工程的YApi接口菜单
    Api.getService(ApiService::class.java, baseUri).getCatMenu(params)
        .subscribeOn(Schedulers.io())
        .doOnError {
            MyNotifier.notifyError(project, "${message("notify.getCatMenu.error")}${it}")
        }
        .subscribe {
            //注入到内存当中
            if (it.errcode == 0) {
                Constant.catMenuDataList = it.data as ArrayList<CatMenuData>
                //请求第一个分类下的接口数据
                val catParams = HashMap<String, String>()
                catParams["catid"] = it.data[0]._id.toString()
                catParams["token"] = token
                Api.getService(ApiService::class.java, baseUri).getInterfaceByCat(catParams)
                    .subscribeOn(Schedulers.io())
                    .doOnError {
                        MyNotifier.notifyError(
                            project,
                            "${message("notify.getInterfaceList.error")}${it}"
                        )
                    }
                    .subscribe { it ->
                        if (it.errcode == 0 && it.data.count > 0) {
                            Constant.interfaceList = it.data.list as ArrayList<InterfaceInfo>
                            println("YApi接口已经更新！！！")
                        }
                    }
            }
        }
}
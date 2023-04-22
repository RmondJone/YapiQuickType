package com.guohanlin.ui

import com.alibaba.fastjson.JSON
import com.guohanlin.Constant
import com.guohanlin.model.ProjectSetting
import com.guohanlin.utils.message
import com.intellij.ide.util.PropertiesComponent
import com.intellij.ui.table.JBTable
import javax.swing.ListSelectionModel
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableColumn

/**
 * 注释：项目YApi配置表格
 * 时间：2021/5/21 0021 9:49
 * 作者：郭翰林
 */
class ProjectSettingTable(private var items: ArrayList<ProjectSetting>) : JBTable() {
    private var mAdapter: TableAdapter = TableAdapter()

    init {
        model = mAdapter
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        val projectNameModel = getColumnModel().getColumn(0)
        val projectIdModel = getColumnModel().getColumn(1)
        val projectTokenModel = getColumnModel().getColumn(2)
        setColumnSize(projectNameModel, 100, 200, 100)
        setColumnSize(projectIdModel, 50, 100, 50)
        setColumnSize(projectTokenModel, 550, 750, 550)
    }

    /**
     * 注释：重置数据
     * 时间：2021/5/23 0023 15:54
     * 作者：郭翰林
     */
    fun reset() {
        val config = PropertiesComponent.getInstance().getValue(Constant.YApiProjectSetting)
        items = if (!config.isNullOrEmpty()) {
            JSON.parseArray(config, ProjectSetting::class.java) as ArrayList<ProjectSetting>
        } else {
            arrayListOf(
                ProjectSetting(
                    "Test Project",
                    "143242",
                    "f1b38e99a5b09073635ab6a901dc2af841f0f507db87086678838c237d9d165b"
                )
            )
        }
        mAdapter.fireTableDataChanged()
    }

    /**
     * 注释：获取现在的数据
     * 时间：2021/5/23 0023 15:21
     * 作者：郭翰林
     */
    fun getItems(): List<ProjectSetting> {
        return items
    }

    /**
     * 注释：添加项目配置
     * 时间：2021/5/21 0021 10:02
     * 作者：郭翰林
     */
    fun addProjectSetting() {
        val projectSettingEditor = ProjectSettingEditor(false, ProjectSetting())
        if (projectSettingEditor.showAndGet()) {
            val projectSetting = projectSettingEditor.getProjectSetting()
            items.add(projectSetting)
            mAdapter.fireTableDataChanged()
        }
    }

    /**
     * 注释：移除项目配置
     * 时间：2021/5/21 0021 10:03
     * 作者：郭翰林
     */
    fun removeProjectSetting() {
        if (selectedRowCount != 0) {
            items.removeAt(selectedRow)
        }
        mAdapter.fireTableDataChanged()
    }

    /**
     * 注释：编辑项目配置
     * 时间：2021/5/21 0021 10:03
     * 作者：郭翰林
     */
    fun editProjectSetting() {
        if (selectedRowCount != 0) {
            var projectSetting = items[selectedRow]
            val projectSettingEditor = ProjectSettingEditor(false, projectSetting)
            if (projectSettingEditor.showAndGet()) {
                items[selectedRow] = projectSettingEditor.getProjectSetting()
                mAdapter.fireTableDataChanged()
            }
        }
    }

    /**
     * 注释：设置每列预设宽度
     * 时间：2021/5/21 0021 9:57
     * 作者：郭翰林
     */
    private fun setColumnSize(
        column: TableColumn,
        preferWidth: Int,
        maxWidth: Int,
        minWidth: Int
    ) {
        column.preferredWidth = preferWidth
        column.minWidth = minWidth
        column.maxWidth = maxWidth
    }

    /**
     * 注释：表格适配器
     * 时间：2021/5/21 0021 9:59
     * 作者：郭翰林
     */
    inner class TableAdapter : AbstractTableModel() {
        override fun getRowCount(): Int {
            return items.size
        }

        override fun getColumnCount(): Int {
            return 3
        }

        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
            val item = items[rowIndex]
            when (columnIndex) {
                0 -> return item.projectName
                1 -> return item.projectId
                2 -> return item.projectToken
            }
            return null
        }

        override fun getColumnName(column: Int): String {
            when (column) {
                0 -> return message("setting.project.name")
                1 -> return message("setting.project.id")
                2 -> return message("setting.project.token")
            }
            return super.getColumnName(column)
        }
    }
}


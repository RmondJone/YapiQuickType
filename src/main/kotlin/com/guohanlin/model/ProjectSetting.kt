package com.guohanlin.model

data class ProjectSetting(
    var projectName: String = "",
    var projectId: String = "",
    var projectToken: String = ""

) {
    override fun toString(): String {
        return projectName
    }
}
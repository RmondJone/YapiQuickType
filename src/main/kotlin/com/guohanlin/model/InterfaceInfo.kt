package com.guohanlin.model

data class InterfaceInfo(
    val _id: Int,
    val add_time: Int,
    val api_opened: Boolean,
    val catid: Int,
    val edit_uid: Int,
    val method: String,
    val path: String,
    val project_id: Int,
    val status: String,
    val tag: List<Any>,
    val title: String,
    val uid: Int
) {
    override fun toString(): String {
        return title
    }
}
package com.guohanlin.model

data class CatMenuData(
    val __v: Int,
    val _id: Int,
    val add_time: Int,
    val desc: String,
    val index: Int,
    val name: String,
    val project_id: Int,
    val uid: Int,
    val up_time: Int


) {
    override fun toString(): String {
        return name
    }
}
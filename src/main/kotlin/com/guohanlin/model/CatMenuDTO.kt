package com.guohanlin.model

data class CatMenuDTO(
    val data: List<CatMenuData>,
    val errcode: Int,
    val errmsg: String
)
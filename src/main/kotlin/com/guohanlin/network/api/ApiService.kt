package com.guohanlin.network.api

import com.guohanlin.model.CatMenuDTO
import com.guohanlin.model.InterfaceByCatDTO
import com.guohanlin.model.InterfaceDetailInfoDTO
import com.guohanlin.model.InterfaceResponseDTO
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {
    /**
     * 注释：获取分类菜单
     * 时间：2021/5/24 0024 10:10
     * 作者：郭翰林
     */
    @GET("/api/interface/getCatMenu")
    fun getCatMenu(@QueryMap params: Map<String, String>): Observable<CatMenuDTO>

    /**
     * 注释：获取某个分类下的接口列表
     * 时间：2021/5/24 0024 11:01
     * 作者：郭翰林
     */
    @GET("/api/interface/list_cat?page=1&limit=100")
    fun getInterfaceByCat(@QueryMap params: Map<String, String>): Observable<InterfaceByCatDTO>

    /**
     * 注释：获取接口详细文档
     * 时间：2021/5/26 0026 17:43
     * 作者：郭翰林
     */
    @GET("/api/interface/get")
    fun getInterfaceDetail(@QueryMap params: Map<String, String>): Observable<InterfaceDetailInfoDTO>

    /**
     * 注释：获取接口返回对应实体
     * 时间：2021/8/31 0031 15:47
     * 作者：郭翰林
     */
    @POST("/api/quickType/conversion")
    @FormUrlEncoded
    fun getInterfaceModel(@FieldMap params: Map<String, String>): Observable<InterfaceResponseDTO>
}
package com.guohanlin.network.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object Api {
    /**
     * 注释：获取Retrofit Service
     * 时间：2021/5/24 0024 10:11
     * 作者：郭翰林
     */
    fun <T> getService(clazz: Class<T>, baseUrl: String): T {
        //创建OkhttpClient
        val okhttpBuilder = OkHttpClient.Builder()
        //适配Https
        val sslContext = SSLContext.getInstance("TLS")
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManager = trustManagerFactory.trustManagers[0] as X509TrustManager
        sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
        okhttpBuilder.connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
        val okhttpClient = okhttpBuilder.build()
        //创建Retrofit客户端
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okhttpClient)
            .build()
        //创建并返回Service
        return retrofit.create(clazz)
    }
}
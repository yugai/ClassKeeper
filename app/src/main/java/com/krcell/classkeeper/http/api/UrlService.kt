package com.krcell.classkeeper.http.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface UrlService {
    @POST
    fun postApi(@Url url: String, @QueryMap maps: Map<String, String> = HashMap()): Observable<String>

    @GET
    fun getApi(@Url url: String): Observable<String>
}

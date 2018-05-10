package com.ramkishorevs.graphqlretrofitconverter

import com.ramkishorevs.graphqlconverter.converter.QueryContainerBuilder
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by ramkishorevs on 10/05/18.
 */

interface ApiInterface {
    @POST("graphql")
    fun getPostsDetails(@Body query: QueryContainerBuilder): Call<Data>
}
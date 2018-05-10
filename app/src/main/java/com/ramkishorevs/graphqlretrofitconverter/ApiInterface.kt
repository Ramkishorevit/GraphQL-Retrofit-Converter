package com.ramkishorevs.graphqlretrofitconverter

import com.ramkishorevs.graphqlconverter.converter.GraphQuery
import com.ramkishorevs.graphqlconverter.converter.QueryContainerBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *
 * Created by ramkishorevs on 10/05/18.
 */

interface ApiInterface {
    @POST("graphql")
    @GraphQuery("sample")
    fun getPostsDetails(@Body query: QueryContainerBuilder): Call<ResponseBody>
}
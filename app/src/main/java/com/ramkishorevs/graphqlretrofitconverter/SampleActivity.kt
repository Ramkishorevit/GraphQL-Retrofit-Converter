package com.ramkishorevs.graphqlretrofitconverter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ramkishorevs.graphqlconverter.converter.GraphQLConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by ramkishorevs on 10/05/18.
 */

class SampleActivity : AppCompatActivity() {
    var BASE_URL = "https://1jzxrj179.lp.gql.zone/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GraphQLConverter.create(this))
                .client(client)
                .build()

    }
}
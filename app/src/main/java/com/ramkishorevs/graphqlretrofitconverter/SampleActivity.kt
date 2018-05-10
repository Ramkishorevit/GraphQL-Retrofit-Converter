package com.ramkishorevs.graphqlretrofitconverter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ramkishorevs.graphqlconverter.converter.GraphQLConverter
import com.ramkishorevs.graphqlconverter.converter.QueryContainerBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit

/**
 * Created by ramkishorevs on 10/05/18.
 *
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

        val apiInterface = retrofit.create(ApiInterface::class.java)
        val queryContainer = QueryContainerBuilder()
        queryContainer.putVariable("id",1)

        val userCall = apiInterface.getPostsDetails(queryContainer)
        userCall.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, post: retrofit2.Response<Post>) {
                val posts = post.body()
                if (posts != null) {
                    author.setText(posts.data.author.firstName)
                    titles.setText(posts.data.author.posts!!.get(0).title)
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                //handle failure
                Toast.makeText(this@SampleActivity,"Oops something went wrong !",
                        Toast.LENGTH_SHORT).show()
            }
        })
    }
}
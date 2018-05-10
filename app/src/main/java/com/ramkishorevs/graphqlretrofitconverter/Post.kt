package com.ramkishorevs.graphqlretrofitconverter

/**
 * Created by ramkishorevs on 10/05/18.
 *
 */

class Post {
    val data = Data()

    class Data {
        val author = Author()

        class Author {
            val firstName: String = ""
            val posts: List<Posts>? = null

            class Posts {
                val title: String = ""
                val votes: Int = 0
            }
        }
    }
}
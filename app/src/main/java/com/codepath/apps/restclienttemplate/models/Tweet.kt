package com.codepath.apps.restclienttemplate.models;

import android.os.Parcelable
import com.codepath.apps.restclienttemplate.TimeFormatter
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject;

@Parcelize
class Tweet(
    var body: String = "",
    var createdAt: String = "",
    var user: User? = null,
    var time: String = ""
) :
    Parcelable {

    companion object {
        fun fromJson(jsonObject: JSONObject): Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            tweet.time = TimeFormatter.getTimeDifference(jsonObject.getString("created_at"))
            return tweet
        }

        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }
    }
}

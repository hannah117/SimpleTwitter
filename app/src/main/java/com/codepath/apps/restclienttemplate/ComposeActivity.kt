package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var charCount: TextView
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.editTextCompose)
        charCount = findViewById(R.id.charCount)
        btnTweet = findViewById(R.id.btnTweet)
        client = TwitterApplication.getRestClient(this)

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.e(TAG, "ontext")
                charCount.text = "${etCompose.text.length}/280"
            }

            override fun afterTextChanged(s: Editable) {
                Log.e(TAG, "aftertext")
                charCount.text = "${etCompose.text.length}/280"
                if(etCompose.text.length > 280) {
                    charCount.setTextColor(Color.RED)
                } else charCount.setTextColor(Color.GRAY)

            }
        })
        btnTweet.setOnClickListener {
            val tweetContent = etCompose.text.toString()

            if (tweetContent.isEmpty()) {
                Toast.makeText(this, "Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
            } else if (tweetContent.length > 280) {
                Log.i(TimelineActivity.TAG, tweetContent.length.toString())

                Toast.makeText(
                    this,
                    "Tweet is too long! Limit is 140 characters",
                    Toast.LENGTH_SHORT
                ).show()
                //make post api call
            } else {
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.e(TAG, "Successfully published a tweet")
                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()

                    }

                })
            }
        }
    }

        companion object {
            val TAG = "ComposeActivity"
        }
    }



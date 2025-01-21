package com.example.api

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityApi : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api)
        val btn=findViewById<Button>(R.id.button3)

        // Make the POST request
        val apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        // Prepare the "data" map

        // Prepare the "data" map
         val data=Data("aa","ff",12.0,1222)

        // Create the PostData object

        // Create the PostData object
        val postData = apimodel( "","Apple MacBook Pro 16","",data)
        val postData1 = apimodel( "","Apple ","",data)

        // Make the POST request

        // Make the POST request

        btn.setOnClickListener {
            apiService.createObject(postData).enqueue(object : Callback<apimodel?> {
                override fun onResponse(call: Call<apimodel?>, response: Response<apimodel?>) {
                    Log.e("TAG", "onResponse:${response.body()} ")
                    Log.e("TAG", "onResponse:${response.code()} ")
                    apiService.updateObject(response.body()?.id,postData1).enqueue(object : Callback<apimodel?> {
                        override fun onResponse(
                            call: Call<apimodel?>,
                            response: Response<apimodel?>,
                        ) {
                            Log.e("RESPONSE", "onResponse:>>${response.body()} ", )
                        }

                        override fun onFailure(call: Call<apimodel?>, t: Throwable) {

                        }
                    })

                }

                override fun onFailure(call: Call<apimodel?>, t: Throwable) {

                }
            })
        }

    }
}
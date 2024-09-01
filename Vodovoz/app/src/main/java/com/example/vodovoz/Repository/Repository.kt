package com.example.vodovoz.Repository

import com.example.vodovoz.Data.Products
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://szorin.vodovoz.ru/"

class Repository{
     object RetrofitInstance{
          private val retrofit = Retrofit.Builder()
               .baseUrl(BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .build()

          val searchProducts = retrofit.create(SearchProducts::class.java)
     }
}

interface SearchProducts{
     @GET("newmobile/glavnaya/super_top.php?action=topglav")
     suspend fun getProducts(): Products
}
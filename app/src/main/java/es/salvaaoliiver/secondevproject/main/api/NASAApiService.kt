package es.salvaaoliiver.secondevproject.main.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("planetary/apod")
    suspend fun getPicturesOfPreviousDays(
        @Query("api_key") apiKey: String,
        @Query("count") count: Int,
        @Query("thumbs") thumbs: Boolean = true
    ): Response<List<NasaPictureOfDay>>
}
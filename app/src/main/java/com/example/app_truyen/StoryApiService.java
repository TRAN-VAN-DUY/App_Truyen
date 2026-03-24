package com.example.app_truyen;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StoryApiService {
    @GET("api/v1/stories/search")
    Call<ApiResponse<SearchResponse>> searchStories(@Query("keyword") String keyword);

    @GET("api/v1/stories")
    Call<ApiResponse<SearchResponse>> getStories(@Query("page") int page, @Query("size") int size);
}

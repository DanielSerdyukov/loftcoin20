package com.lofschool.loftcoin.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CmcApi {

    String API_KEY = "X-CMC_PRO_API_KEY";

    @GET("cryptocurrency/listings/latest")
    Call<Listings> listings(@Query("convert") String convert);

}

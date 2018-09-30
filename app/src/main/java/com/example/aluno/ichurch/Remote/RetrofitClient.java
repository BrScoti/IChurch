package com.example.aluno.ichurch.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aluno on 20/06/2018.
 */

public class RetrofitClient {
    private static Retrofit retrofit= null;
    public static  Retrofit getClient(String baseUrl){
        if(retrofit==null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

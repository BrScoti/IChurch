package com.example.aluno.ichurch.Remote;

import com.example.aluno.ichurch.Model.MyPlaces;
import com.example.aluno.ichurch.Model.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by aluno on 20/06/2018.
 */

public interface IGoogleAPIService {
    @GET
    Call<MyPlaces> getNearbyPlaces (@Url String url);


    @GET
    Call<PlaceDetail> getDetailPlace (@Url String url);

}

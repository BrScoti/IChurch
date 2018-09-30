package com.example.aluno.ichurch;

import com.example.aluno.ichurch.Model.MyPlaces;
import com.example.aluno.ichurch.Model.Results;
import com.example.aluno.ichurch.Remote.IGoogleAPIService;
import com.example.aluno.ichurch.Remote.RetrofitClient;

/**
 * Created by aluno on 20/06/2018.
 */

public class Common {


    public static Results currentResult;
    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService(){
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}

package com.graduation.academic.as.api;

import com.graduation.academic.as.App;
import com.graduation.academic.as.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {

    // static single variable for retrofit client
    // to make ability to overload getClient()
    private static Retrofit retrofit;

    // our backend base url without any end point
    private static String baseUrl;

    // get a built instance of retrofit for use
    public static Retrofit getClient() {

         /*
        do logic here ( add common headers and auth stuff )
         */

        // get base url from resource file
        baseUrl = App.getInstance().getApplicationContext().getString(R.string.base_url);

        // build retrofit client
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()) // for ease use of json parsing 
                .baseUrl(baseUrl)
                .build();
        return retrofit;
    }


}

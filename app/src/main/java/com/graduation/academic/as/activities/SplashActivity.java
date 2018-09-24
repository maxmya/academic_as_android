package com.graduation.academic.as.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.TextView;

import com.graduation.academic.as.R;
import com.graduation.academic.as.api.Api;
import com.graduation.academic.as.api.ApiHelper;
import com.graduation.academic.as.api.MultipleResource;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    Api api;
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // this is how to create instance of api interface
        api = ApiHelper.getClient().create(Api.class);
        responseText = (TextView) findViewById(R.id.responseText);

        /**
         GET List Resources
         **/
        retrofit2.Call<MultipleResource> call = api.doGetListResources();
        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(retrofit2.Call<MultipleResource> call, Response<MultipleResource> response) {
                Log.d("TAG", response.code() + "");

                String displayResponse = "";

                MultipleResource resource = response.body();
                Integer text = resource.page;
                Integer total = resource.total;
                Integer totalPages = resource.totalPages;
                List<MultipleResource.Datum> datumList = resource.data;

                displayResponse += text + " Page\n" + total + " Total\n" + totalPages + " Total Pages\n";

                for (MultipleResource.Datum datum : datumList) {
                    displayResponse += datum.id + " " + datum.name + " " + datum.pantoneValue + " " + datum.year + "\n";
                }

                responseText.setText(displayResponse);

            }

            @Override
            public void onFailure(retrofit2.Call<MultipleResource> call, Throwable t) {

                call.cancel();
            }
        });
        }

    }



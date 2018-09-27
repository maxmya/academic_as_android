package com.graduation.academic.as.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.graduation.academic.as.R;
import com.graduation.academic.as.api.Api;
import com.graduation.academic.as.api.ApiHelper;
import com.graduation.academic.as.api.MultipleResource;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Api api;
    private TextView responseText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        api = ApiHelper.getClient().create(Api.class);
        responseText = (TextView) findViewById(R.id.responseText);

        new ApiTask().execute();


    }

    @SuppressLint("StaticFieldLeak")
    class ApiTask extends AsyncTask<Void, Void, Response<MultipleResource>> {

        @Override
        protected Response<MultipleResource> doInBackground(Void... voids) {
            Response<MultipleResource> call = null;
            try {
                call = api.doGetListResources().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return call;
        }

        @Override
        protected void onPostExecute(Response<MultipleResource> multipleResourceResponse) {
            super.onPostExecute(multipleResourceResponse);

            String displayResponse = "";

            MultipleResource resource = multipleResourceResponse.body();
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
    }
}



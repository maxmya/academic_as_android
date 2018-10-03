package com.graduation.academic.as.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.graduation.academic.as.App;
import com.graduation.academic.as.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoadingButton lb;
    private EditText email;
    private EditText password;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    void initViews() {
        lb = (LoadingButton) findViewById(R.id.loading_btn);
        lb.setOnClickListener(this);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loading_btn) {
            lb.startLoading();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (validFields()) {
                        firebaseLoginTask();
                    } else {
                        lb.cancelLoading();
                        Toast.makeText(LoginActivity.this, "Fill Fields Please", Toast.LENGTH_SHORT).show();
                    }

                }
            }, 3000);

        }
    }

    private boolean validFields() {
        return !TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(password.getText());
    }


    private void firebaseLoginTask() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        lb.loadingSuccessful();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                App.saveLoginToPrefs();
                                App.openActivityWithFadeAnim(Home.class, LoginActivity.this, true);
                            }
                        }, 2000);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                lb.cancelLoading();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

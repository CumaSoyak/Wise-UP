package com.example.cuma.tinder.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cuma.tinder.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    String kullanıcı_adi, kullanıcı_adi2;
    private EditText email;
    private EditText parola;
    private View mProgressView;
    private View mLoginFormView;
    Button giris_buton, kayit_buton;

    private ProgressBar progressBar;
    private CallbackManager callbackManager;
    private AccessToken facebookaccessToken;
    private LoginButton login_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();


        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


        giris_buton = (Button) findViewById(R.id.sign_in);
        kayit_buton = (Button) findViewById(R.id.register);


        email = (EditText) findViewById(R.id.email);
        parola = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        //   mProgressView = findViewById(R.id.login_progress);
        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.bluedark));

        //facebook_kayit();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(ıntent);
        }
    }


    public void register(View view) {

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), parola.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            //   Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_LONG).show();
                            Intent ıntent = new Intent(getApplicationContext(), GecisActivity.class);
                            startActivity(ıntent);

                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bilgileri_kontrol), Toast.LENGTH_LONG).show();
                Log.i("Hata", ":" + e.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    public void signin(View view) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), parola.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);

                            Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(ıntent);

                        }

                        // ...
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Login", "Error" + e.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.e_mailveparolayanlis), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    /*private void facebook_kayit() {
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        login_facebook = (LoginButton) findViewById(R.id.facebook);
        login_facebook.setReadPermissions("email", "public_profile");
        login_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login", "facebook:onSuccess:" + loginResult);
                //handleFacebookAccessToken(loginResult.getAccessToken());
                Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(ıntent);

            }

            @Override
            public void onCancel() {
                Log.d("Login", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Login", "facebook:onError", error);
                // ...
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }






}


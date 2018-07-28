package com.example.cuma.tinder.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

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


import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText parola;
    private View mProgressView;
    private View mLoginFormView;
    Button giris_buton,kayit_buton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private CallbackManager callbackManager;
    private AccessToken facebookaccessToken;
    private LoginButton login_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


        giris_buton = (Button) findViewById(R.id.sign_in);
        kayit_buton=(Button)findViewById(R.id.register);

        if (internet_kontrol()==true){//TODO burası false olacak
            internet_dialog();

        }
        email = (EditText)findViewById(R.id.email);
        parola = (EditText) findViewById(R.id.password);
        progressBar=(ProgressBar)findViewById(R.id.login_progress);
        mProgressView = findViewById(R.id.login_progress);
        facebook_kayit();


    }
/*
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent ıntent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(ıntent);
        }
    }
    */

    public  void  register(View view){
        //TODO burda eğer böyle bir kullanıcı yoksa kayıt yaptırılacak
        //TODO kontrol yap zaten böyle bir kullanıcı var diye uyarı çıkart
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),parola.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("dda", "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(),"Başarılı",Toast.LENGTH_LONG).show();
                            Intent ıntent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(ıntent);
                            // progressBar.setVisibility(View.VISIBLE);

                        }

                        // ...
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                Log.i("Hata",":"+e.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
            }
        });


    }
    public void  signin(View view){
        //TODO burda giriş yapmak isterse böyle bir kullanıcı varsa kontrol yapılacak doğruysa giriş yaptırılacak
        //TODO eğer kullanıcı yoksa kayıt olmak istermisiniz diye popup açılacak
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email.getText().toString(), parola.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login Activity", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent ıntent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(ıntent);

                        }

                        // ...
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                   Log.d("Login","Error"+e.getLocalizedMessage());
                    progressBar.setVisibility(View.GONE);

            }
        });
    }
    private void facebook_kayit(){
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        login_facebook=(LoginButton)findViewById(R.id.facebook);
        login_facebook.setReadPermissions("email", "public_profile");
        login_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login", "facebook:onSuccess:" + loginResult);
                //handleFacebookAccessToken(loginResult.getAccessToken());
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    public boolean internet_kontrol() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    public void internet_dialog(){
        Dialog kontrol=new Dialog(this,R.style.DialogNotitle);
        kontrol.setContentView(R.layout.dialog_internet);
        kontrol.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        kontrol.getWindow().getAttributes().windowAnimations=R.style.Anasayfa_dilog_animasyonu;
        kontrol.setCancelable(false);
        kontrol.show();

    }

}


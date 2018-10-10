package com.example.cuma.tinder.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class GecisActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    private Intent ıntent;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private int progress_deger;
    private View mProgressView;
    Button tamam;
    EditText al_kullanici_adi;
    private TextView guc_text;
    private ImageView savas1, savas2, savas3, savas4, savas5, savas6;
    int deger = 0;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecis);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.bluedark));


        tamam = (Button) findViewById(R.id.gecis_tamam);
        al_kullanici_adi = (EditText) findViewById(R.id.kullanici_adi_editext);
        progressBar = (ProgressBar) findViewById(R.id.gecis_progressBar);
        guc_text = (TextView) findViewById(R.id.guc_text);
        guc_text.setVisibility(View.INVISIBLE);
        guc_text.setText(getResources().getString(R.string.guc_baslik));
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

        savas1 = findViewById(R.id.savas1);
        savas2 = findViewById(R.id.savas2);
        savas3 = findViewById(R.id.savas3);
        savas4 = findViewById(R.id.savas4);
        savas5 = findViewById(R.id.savas5);
        savas6 = findViewById(R.id.savas6);

        tamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (al_kullanici_adi.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.kullanici_adi_gir), Toast.LENGTH_LONG).show();
                } else if (deger == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.karakter_sec), Toast.LENGTH_LONG).show();

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    guc_text.setVisibility(View.VISIBLE);
                    ıntent = new Intent(getApplicationContext(), MainActivity.class);
                    ekle_Firebase_kullanici_adi();
                    Thread timer = new Thread() {
                        public void run() {
                            while (progress_deger < 100) {
                                progress_deger++;
                                android.os.SystemClock.sleep(50);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setProgress(progress_deger);
                                    }
                                });
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(ıntent);
                                }
                            });

                        }
                    };
                    timer.start();


                }
            }
        });
    }

    public void savasci_sec(View view) {
        switch (view.getId()) {
            case R.id.savas1:
                databaseReference.child("Kullanıcı_Adı").child(user_id).child("photo").setValue(1);
                deger = 1;
                break;
            case R.id.savas2:
                databaseReference.child("Kullanıcı_Adı").child(user_id).child("photo").setValue(2);
                deger = 2;
                break;
            case R.id.savas3:
                databaseReference.child("Kullanıcı_Adı").child(user_id).child("photo").setValue(3);
                deger = 3;
                break;
            case R.id.savas4:
                databaseReference.child("Kullanıcı_Adı").child(user_id).child("photo").setValue(4);
                deger = 4;
                break;
            case R.id.savas5:
                databaseReference.child("Kullanıcı_Adı").child(user_id).child("photo").setValue(5);
                deger = 5;
                break;
            case R.id.savas6:
                databaseReference.child("Kullanıcı_Adı").child(user_id).child("photo").setValue(6);
                deger = 6;
                break;

        }
        secilmemis_hale_getir();
    }

    public void secilmemis_hale_getir() {
        switch (deger) {
            case 1:
                mediaPlayer = MediaPlayer.create(GecisActivity.this, R.raw.tarih);
                mediaPlayer.start();
                savas1.setImageResource(R.drawable.checked);
                savas2.setImageResource(R.drawable.bilimadami);
                savas3.setImageResource(R.drawable.eglenceadami);
                savas4.setImageResource(R.drawable.cografyadami);
                savas5.setImageResource(R.drawable.sanatadami);
                savas6.setImageResource(R.drawable.sporadami);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(GecisActivity.this, R.raw.bilim);
                mediaPlayer.start();
                savas2.setImageResource(R.drawable.checked);
                savas1.setImageResource(R.drawable.tarihadami);
                savas3.setImageResource(R.drawable.eglenceadami);
                savas4.setImageResource(R.drawable.cografyadami);
                savas5.setImageResource(R.drawable.sanatadami);
                savas6.setImageResource(R.drawable.sporadami);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(GecisActivity.this, R.raw.eglence);
                mediaPlayer.start();
                savas3.setImageResource(R.drawable.checked);
                savas1.setImageResource(R.drawable.tarihadami);
                savas2.setImageResource(R.drawable.bilimadami);
                savas4.setImageResource(R.drawable.cografyadami);
                savas5.setImageResource(R.drawable.sanatadami);
                savas6.setImageResource(R.drawable.sporadami);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(GecisActivity.this, R.raw.cografya);
                mediaPlayer.start();
                savas4.setImageResource(R.drawable.checked);
                savas2.setImageResource(R.drawable.bilimadami);
                savas3.setImageResource(R.drawable.eglenceadami);
                savas1.setImageResource(R.drawable.tarihadami);
                savas5.setImageResource(R.drawable.sanatadami);
                savas6.setImageResource(R.drawable.sporadami);
                break;
            case 5:
                mediaPlayer = MediaPlayer.create(GecisActivity.this, R.raw.sanat);
                mediaPlayer.start();
                savas5.setImageResource(R.drawable.checked);
                savas2.setImageResource(R.drawable.bilimadami);
                savas3.setImageResource(R.drawable.eglenceadami);
                savas4.setImageResource(R.drawable.cografyadami);
                savas1.setImageResource(R.drawable.tarihadami);
                savas6.setImageResource(R.drawable.sporadami);
                break;
            case 6:
                mediaPlayer = MediaPlayer.create(GecisActivity.this, R.raw.spor);
                mediaPlayer.start();
                savas6.setImageResource(R.drawable.checked);
                savas2.setImageResource(R.drawable.bilimadami);
                savas3.setImageResource(R.drawable.eglenceadami);
                savas4.setImageResource(R.drawable.cografyadami);
                savas5.setImageResource(R.drawable.sanatadami);
                savas1.setImageResource(R.drawable.tarihadami);
                break;

        }

    }

    private void ekle_Firebase_kullanici_adi() {
        user = firebaseAuth.getCurrentUser();
        String useremail = user.getEmail().toString();
        databaseReference.child("Kullanıcı_Adı").child(user_id).child("nickname").setValue(al_kullanici_adi.getText().toString());
        databaseReference.child("Yarisma").child(user_id).child("nickname").setValue(al_kullanici_adi.getText().toString());
        databaseReference.child("Yarisma").child(user_id).child("siralama").setValue(0); //default olarak değer atadık
        databaseReference.child("Puanlar").child(user_id).child("kalp").setValue(5);
        databaseReference.child("Puanlar").child(user_id).child("para").setValue(0);
        databaseReference.child("Puanlar").child(user_id).child("elmas").setValue(0);
        databaseReference.child("Yarisma").child(user_id).child("siralama").setValue(100000);

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);


    }


}

package com.example.cuma.tinder.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneriFragment extends Fragment implements View.OnClickListener {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    private Button oneri_gonder;
    private EditText oneri_soru;
    private RadioButton evet, hayir;
    private ImageButton onerildi_soru_thanks;
    int radiobuton_id;
    private int deger, kategori_deger = 0;
    private ImageView kategori1, kategori2, kategori3, kategori4, kategori5, kategori6;

    public OneriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oneri, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

        oneri_gonder = (Button) view.findViewById(R.id.oneri_gonder);
        oneri_soru = (EditText) view.findViewById(R.id.oneri_soru);
        evet = (RadioButton) view.findViewById(R.id.evet);
        hayir = (RadioButton) view.findViewById(R.id.hayir);
        kategori1 = view.findViewById(R.id.kategori1);
        kategori1.setOnClickListener(this);
        kategori2 = view.findViewById(R.id.kategori2);
        kategori2.setOnClickListener(this);
        kategori3 = view.findViewById(R.id.kategori3);
        kategori3.setOnClickListener(this);
        kategori4 = view.findViewById(R.id.kategori4);
        kategori4.setOnClickListener(this);
        kategori5 = view.findViewById(R.id.kategori5);
        kategori5.setOnClickListener(this);
        kategori6 = view.findViewById(R.id.kategori6);
        kategori6.setOnClickListener(this);
        oneri_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Locale.getDefault().getLanguage().equals("tr")) {
                    Firebase_kaydet_soru_turkce();

                } else {
                    Firebase_kaydet_soru_ingilizce();
                }

            }
        });

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.kategori1:
                kategori_deger = 1;
                break;
            case R.id.kategori2:
                kategori_deger = 2;
                break;
            case R.id.kategori3:
                kategori_deger = 3;
                break;
            case R.id.kategori4:
                kategori_deger = 4;
                break;
            case R.id.kategori5:
                kategori_deger = 5;
                break;
            case R.id.kategori6:
                kategori_deger = 6;
                break;

        }

        secilmemis_hale_getir();

    }

    public void secilmemis_hale_getir() {
        switch (kategori_deger) {
            case 1:
                kategori1.setImageResource(R.drawable.checked);
                kategori2.setImageResource(R.drawable.bilim);
                kategori3.setImageResource(R.drawable.eglence);
                kategori4.setImageResource(R.drawable.cografya);
                kategori5.setImageResource(R.drawable.sanat);
                kategori6.setImageResource(R.drawable.spor);
                break;
            case 2:
                kategori2.setImageResource(R.drawable.checked);
                kategori1.setImageResource(R.drawable.tarihim);
                kategori3.setImageResource(R.drawable.eglence);
                kategori4.setImageResource(R.drawable.cografya);
                kategori5.setImageResource(R.drawable.sanat);
                kategori6.setImageResource(R.drawable.spor);
                break;
            case 3:
                kategori3.setImageResource(R.drawable.checked);
                kategori1.setImageResource(R.drawable.tarihim);
                kategori2.setImageResource(R.drawable.bilim);
                kategori4.setImageResource(R.drawable.cografya);
                kategori5.setImageResource(R.drawable.sanat);
                kategori6.setImageResource(R.drawable.spor);
                break;
            case 4:
                kategori4.setImageResource(R.drawable.checked);
                kategori2.setImageResource(R.drawable.bilim);
                kategori3.setImageResource(R.drawable.eglence);
                kategori1.setImageResource(R.drawable.tarihim);
                kategori5.setImageResource(R.drawable.sanat);
                kategori6.setImageResource(R.drawable.spor);
                break;
            case 5:
                kategori5.setImageResource(R.drawable.checked);
                kategori2.setImageResource(R.drawable.bilim);
                kategori3.setImageResource(R.drawable.eglence);
                kategori4.setImageResource(R.drawable.cografya);
                kategori1.setImageResource(R.drawable.tarihim);
                kategori6.setImageResource(R.drawable.spor);
                break;
            case 6:
                kategori6.setImageResource(R.drawable.checked);
                kategori2.setImageResource(R.drawable.bilim);
                kategori3.setImageResource(R.drawable.eglence);
                kategori4.setImageResource(R.drawable.cografya);
                kategori5.setImageResource(R.drawable.sanat);
                kategori1.setImageResource(R.drawable.tarihim);
                break;

        }

    }

    private void Firebase_kaydet_soru_turkce() {
        UUID uuıd = UUID.randomUUID();
        uuid_String = uuıd.toString();
        user = firebaseAuth.getCurrentUser();
        if (networkConnection()) {
            if (oneri_soru.getText().toString().matches("") || kategori_deger == 0) {
                Toast.makeText(getActivity(), getResources().getString(R.string.alanlari_bos_gecme), Toast.LENGTH_LONG).show();
                return;
            } else if (evet.isChecked()) {
                databaseReference.child("Sorular_turkce").child(String.valueOf(kategori_deger)).child(uuid_String).child("soru").setValue(oneri_soru.getText().toString());
                databaseReference.child("Sorular_turkce").child(String.valueOf(kategori_deger)).child(uuid_String).child("cevap").setValue("Yes");
                tesekur_dialog_goster();
                oneri_soru.getText().clear();
            } else if (hayir.isChecked()) {
                databaseReference.child("Sorular_turkce").child(String.valueOf(kategori_deger)).child(uuid_String).child("soru").setValue(oneri_soru.getText().toString());
                databaseReference.child("Sorular_turkce").child(String.valueOf(kategori_deger)).child(uuid_String).child("cevap").setValue("No");
                tesekur_dialog_goster();
                oneri_soru.getText().clear();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.alanlari_bos_gecme), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_text), Toast.LENGTH_LONG).show();

        }
    }

    private void Firebase_kaydet_soru_ingilizce() {
        UUID uuıd = UUID.randomUUID();
        uuid_String = uuıd.toString();
        user = firebaseAuth.getCurrentUser();
        if (networkConnection()) {
            if (oneri_soru.getText().toString().matches("") || kategori_deger == 0) {
                Toast.makeText(getActivity(), getResources().getString(R.string.alanlari_bos_gecme), Toast.LENGTH_LONG).show();
                return;
            } else if (evet.isChecked()) {
                databaseReference.child("Sorular_ingilizce").child(String.valueOf(kategori_deger)).child(uuid_String).child("soru").setValue(oneri_soru.getText().toString());
                databaseReference.child("Sorular_ingilizce").child(String.valueOf(kategori_deger)).child(uuid_String).child("cevap").setValue("Yes");
                tesekur_dialog_goster();
                oneri_soru.getText().clear();
            } else if (hayir.isChecked()) {
                databaseReference.child("Sorular_ingilizce").child(String.valueOf(kategori_deger)).child(uuid_String).child("soru").setValue(oneri_soru.getText().toString());
                databaseReference.child("Sorular_ingilizce").child(String.valueOf(kategori_deger)).child(uuid_String).child("cevap").setValue("No");
                tesekur_dialog_goster();
                oneri_soru.getText().clear();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.alanlari_bos_gecme), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_text), Toast.LENGTH_LONG).show();

        }
    }

    public void tesekur_dialog_goster() {
        final Dialog basarili_dialog = new Dialog(getActivity(), R.style.DialogNotitle);
        basarili_dialog.setContentView(R.layout.dialog_oneri_basarili);
        basarili_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        basarili_dialog.getWindow().getAttributes().windowAnimations = R.style.Anasayfa_dilog_animasyonu;
        onerildi_soru_thanks = (ImageButton) basarili_dialog.findViewById(R.id.onerildi_soru_thanks);
        basarili_dialog.show();
        onerildi_soru_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basarili_dialog.dismiss();
            }
        });
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    basarili_dialog.dismiss();
                }
            }
        };

    }

    public boolean networkConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}

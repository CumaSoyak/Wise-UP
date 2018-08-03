package com.example.cuma.tinder.Fragment;


import android.graphics.Color;
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

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneriFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    private Button oneri_gonder;
    private EditText oneri_soru;
    private RadioButton evet, hayir;
    int radiobuton_id;
    private Spinner kategori_spinner;
    private ArrayAdapter<String> spinner_adapter;
    private String[] kategoriler = {"Kategori", "Tarih", "Bilim", "Eğlence", "Coğrafya", "Sanat", "Spor"};
    private String selectedItem;
    private int deger;

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
        kategori_spinner = (Spinner) view.findViewById(R.id.kategori_spinner);
        spinner_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kategoriler) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };//spinnerler için adapterler belırlıyoruz
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//listelenecek verilerin görunumunu belırlıyoruz
        kategori_spinner.setAdapter(spinner_adapter);//hazırladığımız adapterleri sniplera eklıyoruz
        kategori_sec();
        oneri_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Todo eğer sor paylaşımı başarılı olursa dialog açılsın
                if (selectedItem.equals("Kategori")) {
                    Toast.makeText(getActivity(), "Lütfen Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
                } else {
                    Firebase_kaydet_soru();
                }
            }
        });


        return view;
    }

    private void Firebase_kaydet_soru() {
        UUID uuıd = UUID.randomUUID();
        uuid_String = uuıd.toString();
        user = firebaseAuth.getCurrentUser();
        if (oneri_soru.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "Lütfen Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
            return;
        }
        if (evet.isChecked()) {
            databaseReference.child("Sorular").child(String.valueOf(deger)).child(uuid_String).child("soru").setValue(oneri_soru.getText().toString());
            databaseReference.child("Sorular").child(String.valueOf(deger)).child(uuid_String).child("cevap").setValue("Yes");
        } else if (hayir.isChecked()) {
            databaseReference.child("Sorular").child(String.valueOf(deger)).child(uuid_String).child("soru").setValue(oneri_soru.getText().toString());
            databaseReference.child("Sorular").child(String.valueOf(deger)).child(uuid_String).child("cevap").setValue("No");

        } else {
            Toast.makeText(getActivity(), "Lütfen Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
        }
      /*  switch (deger) {
            case 1:
                databaseReference.child("Sorular").child(uuid_String).child("kategori").setValue(1);
                break;
            case 2:
                databaseReference.child("Sorular").child(uuid_String).child("kategori").setValue(2);
                break;
            case 3:
                databaseReference.child("Sorular").child(uuid_String).child("kategori").setValue(3);
                break;
            case 4:
                databaseReference.child("Sorular").child(uuid_String).child("kategori").setValue(4);
                break;
            case 5:
                databaseReference.child("Sorular").child(uuid_String).child("kategori").setValue(5);
                break;
            case 6:
                databaseReference.child("Sorular").child(uuid_String).child("kategori").setValue(6); break;
        } */

    }

    public void kategori_sec() {
        kategori_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                switch (position) {
                    case 1:
                        deger = 1;
                        break;
                    case 2:
                        deger = 2;
                        break;
                    case 3:
                        deger = 3;
                        break;
                    case 4:
                        deger = 4;
                        break;
                    case 5:
                        deger = 5;
                        break;
                    case 6:
                        deger = 6;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

}

package com.example.cuma.tinder.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cuma.tinder.Class.Admin_onay;
import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;


public class AdminFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    private ArrayList<Admin_onay> admin_onay_list = new ArrayList<>();
    private ArrayList<Admin_onay> admin_onay_list2 = new ArrayList<>();
    private Admin_onay admin_onay;
    private EditText onay_edittext_soru, onay_edittext_cevap;
    private Button gec, gonder;
    private int deger;
    private Spinner adimin_spinner;
    private ArrayAdapter<String> kategori_adapter;
    private String[] kategoriler = {"Tarih", "Bilim", "Eğlence", "Coğrafya", "Sanat", "Spor"};
    private int kategori=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();
        onay_edittext_soru = (EditText) view.findViewById(R.id.onay_edittext_soru);
        onay_edittext_cevap = (EditText) view.findViewById(R.id.onay_edittext_cevap);
        gec = (Button) view.findViewById(R.id.admin_gec);
        gonder = (Button) view.findViewById(R.id.admin_gonder);
        adimin_spinner=(Spinner)view.findViewById(R.id.adimin_spinner);
        kategori_adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, kategoriler);
        kategori_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adimin_spinner.setAdapter(kategori_adapter);

        kategori_islemleri();
        veri_tabanı_ismelmleri();

        gec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deger++;
                if (deger < admin_onay_list2.size()) {
                    //  Log.i("Admin", ":" + admin_onay_list2.get(deger).getSoru());
                    onay_edittext_soru.setText(admin_onay_list2.get(deger).getSoru());
                    onay_edittext_cevap.setText(admin_onay_list2.get(deger).getCevap());
                }
                else {
                    Toast.makeText(getActivity(),"Sorular bitti",Toast.LENGTH_LONG).show();
                }

                gonder.setEnabled(true);

            }
        });
        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID uuıd = UUID.randomUUID();
                uuid_String = uuıd.toString();

                if (deger < admin_onay_list2.size()) {
                  //  Log.i("Admin", ":" + admin_onay_list2.get(deger).getSoru());
                    onay_edittext_soru.setText(admin_onay_list2.get(deger).getSoru());
                    onay_edittext_cevap.setText(admin_onay_list2.get(deger).getCevap());
                    databaseReference.child("Sorular").child(String.valueOf(kategori)).child(uuid_String).child("soru").setValue(onay_edittext_soru.getText().toString());
                    databaseReference.child("Sorular").child(String.valueOf(kategori)).child(uuid_String).child("cevap").setValue(onay_edittext_cevap.getText().toString());
                }
                else {
                    Toast.makeText(getActivity(),"Sorular bitti",Toast.LENGTH_LONG).show();
                }
                gonder.setEnabled(false);
            }
        });


        return view;
    }
    private void kategori_islemleri(){
        adimin_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                      kategori=1;
                        break;
                    case 2:
                        kategori=2;
                        break;
                    case 3:
                        kategori=3;
                        break;
                    case 4:
                        kategori=4;
                        break;
                    case 5:
                        kategori=5;
                        break;
                    case 6:
                        kategori=6;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void veri_tabanı_ismelmleri() {

        databaseReference.child("Sorular_Onay").child(String.valueOf(deger)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    admin_onay = ds.getValue(Admin_onay.class);
                    admin_onay_list.add(admin_onay);
                    cagir();
                    // Log.i("Admin",":"+admin_onay.getSoru());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void cagir() {
        admin_onay_list2 = admin_onay_list;

    }


}

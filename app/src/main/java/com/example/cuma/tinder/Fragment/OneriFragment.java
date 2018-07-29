package com.example.cuma.tinder.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


        oneri_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase_kaydet_soru();
                //Todo eğer sor paylaşımı başarılı olursa dialog açılsın


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
        if (evet.isChecked()){
            databaseReference.child("Sorular").child(uuid_String).child("soru").setValue(oneri_soru.getText().toString());
            databaseReference.child("Sorular").child(uuid_String).child("cevap").setValue("Evet");
        }
        else if (hayir.isChecked()){
            databaseReference.child("Sorular").child(uuid_String).child("soru").setValue(oneri_soru.getText().toString());
            databaseReference.child("Sorular").child(uuid_String).child("cevap").setValue("Hayır");

        }
        else {
            Toast.makeText(getActivity(), "Lütfen Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();

        }
    }

}

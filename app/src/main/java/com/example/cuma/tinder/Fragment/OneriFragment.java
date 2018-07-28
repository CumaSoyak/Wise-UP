package com.example.cuma.tinder.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneriFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;

    private Button oneri_gonder;
    private EditText oneri_soru;
    private RadioButton evet,hayir;



    public OneriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oneri, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference();
        user =firebaseAuth.getCurrentUser();
        user_id=user.getUid();

        oneri_gonder=(Button)view.findViewById(R.id.oneri_gonder);
        oneri_soru=(EditText)view.findViewById(R.id.oneri_soru);
        evet=(RadioButton)view.findViewById(R.id.evet);
        hayir=(RadioButton)view.findViewById(R.id.hayir);

        oneri_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Firebase_kaydet_soru();
                    //Todo eğer sor paylaşımı başarılı olursa dialog açılsın


            }
        });



        return view;
    }

    private void Firebase_kaydet_soru(){
        user=firebaseAuth.getCurrentUser();
       databaseReference.child("Sorular").child("soru").setValue("dwadwadawd");
        Toast.makeText(getActivity(),oneri_soru.getText().toString(),Toast.LENGTH_LONG).show();
       if (evet.isChecked()){
           databaseReference.child("Sorular").child("soru").child("cevap").child("cevap1").setValue("Evet");
       }
       if (hayir.isChecked()){
           databaseReference.child("Sorular").child("soru").child("cevap").child("cevap1").setValue("Hayir");       }
    }

}

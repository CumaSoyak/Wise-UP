package com.example.cuma.tinder.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cuma.tinder.Adapter.KupaAdapter;
import com.example.cuma.tinder.Class.Kupa;
import com.example.cuma.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KupaFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    Integer siralama;
    private TextView time;

    private ArrayList<Kupa> kupaList;
    private RecyclerView recyclerView;
    private KupaAdapter kupaAdapter;
    private int rutbe = 0;
    private int[] rutbeler = {R.drawable.rutbe1, R.drawable.rutbe3, R.drawable.rutbe4
            , R.drawable.rutbe6, R.drawable.rutbe7, R.drawable.rutbe8, R.drawable.rutbe9,
            R.drawable.rutbe10, R.drawable.rutbe11, R.drawable.rutbe5};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kupa, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Yarisma");
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();


        time = (TextView) view.findViewById(R.id.time);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_kupa); //Reclerview tanımladık
        kupaList = new ArrayList<Kupa>();         //Arraylist oluşturduk
        kupaAdapter = new KupaAdapter(kupaList);      //Adaptare listeyi atadık

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(kupaAdapter);   //reclerviewe et ettik kupa adapteri
        Firebase_getData();

        return view;
    }


    private void Firebase_getData() {
        Query query = databaseReference.orderByChild("siralama").limitToFirst(10);// ilk 20 kullanıcıyı getirir
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Kupa kupa = ds.getValue(Kupa.class);
                    kupaList.add(new Kupa(kupa.getNickname(), kupa.getPuan(), rutbeler[rutbe]));
                    Log.i("KupaList",":"+kupa.getNickname());
                    rutbe++;
                }


                kupaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}

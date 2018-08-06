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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class KupaFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    Integer siralama;

    private List<Kupa> kupaList;
    private RecyclerView recyclerView;
    private KupaAdapter kupaAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kupa, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Yarisma");
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

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
        Query query = databaseReference.orderByChild("siralama");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Kupa kupa = ds.getValue(Kupa.class); //todo buraya bakmam lazım
                    kupaList.add(new Kupa(kupa.getNickname(), kupa.getSiralama(), R.mipmap.madalyon1));
                }


                kupaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}

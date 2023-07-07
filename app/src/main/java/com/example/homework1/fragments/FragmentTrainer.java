package com.example.homework1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.homework1.R;
import com.example.homework1.models.Trainer;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentTrainer extends Fragment {
    private MaterialTextView trainer_name;
    private MaterialTextView trainer_caught;

    public FragmentTrainer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference trainersRef = db.getReference("trainers");
        String userId = "Moshe";

        trainersRef.orderByChild("name").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Trainer trainer = data.getValue(Trainer.class);
                    trainer_name.setText(trainer.getName());
                    String caught = getString(R.string.trainer_caught_str) + trainer.getAfekaMons();
                    trainer_caught.setText(caught);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void findViews(View view) {
        trainer_name = view.findViewById(R.id.trainer_name);
        trainer_caught = view.findViewById(R.id.trainer_caught);
    }
}
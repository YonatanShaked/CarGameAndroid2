package com.example.homework1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.homework1.R;
import com.example.homework1.models.Trainer;
import com.google.android.material.textview.MaterialTextView;

public class FragmentTrainer extends Fragment {
    private final Trainer trainer;
    private MaterialTextView trainer_name;
    private MaterialTextView trainer_caught;

    public FragmentTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        if (trainer != null) {
            trainer_name.setText(trainer.getName());
            String caught = getString(R.string.trainer_caught_str) + trainer.getAfekaMons();
            trainer_caught.setText(caught);
        }
    }

    private void findViews(View view) {
        trainer_name = view.findViewById(R.id.trainer_name);
        trainer_caught = view.findViewById(R.id.trainer_caught);
    }
}
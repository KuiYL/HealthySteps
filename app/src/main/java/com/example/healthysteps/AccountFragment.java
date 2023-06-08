package com.example.healthysteps;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

import java.util.Objects;

public class AccountFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    Button button;
    TextView textView;

    public AccountFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        auth = FirebaseAuth.getInstance();
        button = view.findViewById(R.id.exitAccount);
        textView = view.findViewById(R.id.users_details);
        user = auth.getCurrentUser();
        if(user  == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().onBackPressed();
        }
        else{
            String text = "Почта: "+user.getEmail();
            textView.setText(text);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().onBackPressed();

            }
        });
        return  view;
    }


}
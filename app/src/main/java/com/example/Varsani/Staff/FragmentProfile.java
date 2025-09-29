package com.example.Varsani.Staff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

public class FragmentProfile extends Fragment {
    private SessionHandler session;
    private UserModel user;
    private TextView txv_name, txv_phoneNo, txv_email,
            txv_username,txv_role;

    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        txv_name = root.findViewById(R.id.txv_name);
        txv_username =root. findViewById(R.id.txv_username);
        txv_phoneNo =root. findViewById(R.id.txv_phoneNo);
        txv_email =root. findViewById(R.id.txv_email);
        txv_role =root. findViewById(R.id.txv_role);

        session = new SessionHandler(getContext());
        user = session.getUserDetails();

        txv_email.setText( user.getEmail());
        txv_name.setText("Name " + user.getFirstname() + " " + user.getLastname());
        txv_phoneNo.setText(user.getPhoneNo());
        txv_username.setText("Username " + user.getUsername());
        txv_role.setText(user.getUser_type());
        return  root;
    }



}

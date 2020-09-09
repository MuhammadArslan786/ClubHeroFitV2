package com.arslan6015.clubherofitv2.ui.contacts;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arslan6015.clubherofitv2.ContactsFriend;
import com.arslan6015.clubherofitv2.R;

public class ContactsFragment extends Fragment {

    LinearLayout firstLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        firstLayout = root.findViewById(R.id.firstLayout);
        firstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ContactsFriend.class));
            }
        });
        return root;
    }
}
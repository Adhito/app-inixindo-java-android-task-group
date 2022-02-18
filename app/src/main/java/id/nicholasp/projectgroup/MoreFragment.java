package id.nicholasp.projectgroup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MoreFragment extends Fragment {
    CardView cv_top_up, cv_about_us, cv_logout, cv_update;
    public static final String SHARED_PREFS = "shared_prefs";
    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        cv_top_up = view.findViewById(R.id.cv_top_up);
        cv_about_us = view.findViewById(R.id.cv_about_us);
        cv_logout = view.findViewById(R.id.cv_logout);
        cv_update = view.findViewById(R.id.cv_Update);

        cv_top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),TopUpActivity.class));
            }
        });

        cv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),UpdateProfileActivity.class));
            }
        });

        cv_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.maybank.co.id/"));
                startActivity(intent);
            }
        });

        cv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getActivity(), LoginFormActivity.class));
                Toast.makeText(getActivity(), "Successfully Logged Out", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
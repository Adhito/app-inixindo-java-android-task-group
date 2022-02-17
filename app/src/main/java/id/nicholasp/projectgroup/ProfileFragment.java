package id.nicholasp.projectgroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    // session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_KEY = "user_key";
    public static final String ID_KEY = "id_key";
    public static final String NAMA_KEY = "nama_key";
    public static final String SID_KEY = "sid_key";
    public static final String EMAIL_KEY = "email_key";
    public static final String HP_KEY = "hp_key";
    SharedPreferences sharedpreferences;
    TextView txt_p_nama,txt_p_user,txt_p_sid,txt_p_email,txt_p_hp;
    String user, nama, sid, email, hp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txt_p_nama = view.findViewById(R.id.txt_p_nama);
        txt_p_user = view.findViewById(R.id.txt_p_user);
        txt_p_sid = view.findViewById(R.id.txt_p_sid);
        txt_p_email = view.findViewById(R.id.txt_p_email);
        txt_p_hp = view.findViewById(R.id.txt_p_hp);

        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        user = sharedpreferences.getString(USER_KEY, null);
        nama = sharedpreferences.getString(NAMA_KEY, null);
        sid = sharedpreferences.getString(SID_KEY, null);
        email = sharedpreferences.getString(EMAIL_KEY, null);
        hp = sharedpreferences.getString(HP_KEY, null);

        txt_p_nama.setText(nama);
        txt_p_user.setText(user);
        txt_p_sid.setText(sid);
        txt_p_email.setText(email);
        txt_p_hp.setText(hp);


        return view;
    }
}
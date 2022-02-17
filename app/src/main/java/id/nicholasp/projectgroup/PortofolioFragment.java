package id.nicholasp.projectgroup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import id.nicholasp.projectgroup.databinding.FragmentPortofolioBinding;

public class PortofolioFragment extends Fragment {

    // session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_KEY = "user_key";
    public static final String ID_KEY = "id_key";
    public static final String NAMA_KEY = "nama_key";
    public static final String SID_KEY = "sid_key";
    public static final String EMAIL_KEY = "email_key";
    public static final String HP_KEY = "hp_key";
    public static final String BALANCE_KEY = "balance_key";

    SharedPreferences sharedpreferences;
    String myStr, sendBalance;
    CardView cvRObligasi;
    TextView txtRNama,txtRSaldo,txtRTotal;
//    FragmentPortofolioBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_portofolio);
        View view = inflater.inflate(R.layout.fragment_portofolio, container, false);

//        Intent intent = getActivity().getIntent();
//        Bundle extras = intent.getExtras();
//        myStr = extras.getString("keyUser");

        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        myStr = sharedpreferences.getString(USER_KEY, null);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(ID_KEY, null);
        editor.putString(NAMA_KEY, null);
        editor.putString(EMAIL_KEY, null);
        editor.putString(SID_KEY, null);
        editor.putString(HP_KEY, null);
        editor.apply();

        cvRObligasi = view.findViewById(R.id.cv_r_obligasi);
        txtRNama = view.findViewById(R.id.txt_r_nama);
        txtRSaldo = view.findViewById(R.id.txt_r_saldo);
        txtRTotal = view.findViewById(R.id.txt_r_total);
        Log.d("user: ", myStr);

        getJSON();



        cvRObligasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "CardView Clicked", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getActivity(), DetailPortofolioActivity.class);
                myIntent.putExtra("keyBalance", sendBalance);
                startActivity(myIntent);
            }
        });

        return view;
    }

    private void getJSON() {
        // batuan dari class AsynTask
        class GetJSON extends AsyncTask<Void,Void,String> { // boleh membuat class dalam method (Inner Class)
            ProgressDialog loading;

            @Override
            protected void onPreExecute() { // sebelum proses
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) { // saat proses
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(ConfigurationPortofolio.URL_GET_USER,myStr);
                Log.d("prof: ",result);
                return result;
            }

            @Override
            protected void onPostExecute(String message) { // setelah proses
                super.onPostExecute(message);
                displayDetailData(message);
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void displayDetailData(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            JSONArray result = jsonObject.getJSONArray(ConfigurationPortofolio.TAG_JSON_ARRAY);
            JSONObject object = result.getJSONObject(0);

            Log.d("msg: ",message);

            String id_user = object.getString(ConfigurationPortofolio.KEY_LOG_DETAIL_ID);
            String nama = object.getString(ConfigurationPortofolio.KEY_LOG_NAMA);
            String balance = object.getString(ConfigurationPortofolio.KEY_LOG_BALANCE);
            String sid = object.getString(ConfigurationPortofolio.KEY_LOG_SID);
            String email = object.getString(ConfigurationPortofolio.KEY_LOG_EMAIL);
            String hp = object.getString(ConfigurationPortofolio.KEY_LOG_HP);
            String total = object.getString(ConfigurationPortofolio.KEY_LOG_TOTAL);
            sendBalance = object.getString(ConfigurationPortofolio.KEY_LOG_TOTAL);

            Log.d("profile: ", "id: " + id_user + " nama: "+nama+" balance: "+balance+
                    " sid: " + sid + " email: " + email + " hp: "+hp);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(ID_KEY, id_user);
            editor.putString(NAMA_KEY, nama);
            editor.putString(EMAIL_KEY, email);
            editor.putString(SID_KEY, sid);
            editor.putString(HP_KEY, hp);
            editor.putString(BALANCE_KEY, balance);
            editor.apply();

            txtRNama.setText(nama);
            txtRSaldo.setText("Rp. "+balance);
            txtRTotal.setText("Rp. "+total);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
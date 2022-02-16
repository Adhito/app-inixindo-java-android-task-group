package id.nicholasp.projectgroup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import id.nicholasp.projectgroup.databinding.FragmentPortofolioBinding;

public class PortofolioFragment extends Fragment {

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String USER_KEY = "user_key";

    SharedPreferences sharedpreferences;
    String myStr, sendBalance;
    FragmentPortofolioBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_portofolio);
        View view = inflater.inflate(R.layout.fragment_portofolio, container, false);

//        Intent intent = getActivity().getIntent();
//        Bundle extras = intent.getExtras();
//        myStr = extras.getString("keyUser");

        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        myStr = sharedpreferences.getString(USER_KEY, null);
        Log.d("user: ", myStr);

        getJSON();

        binding.cvRObligasi.setOnClickListener(new View.OnClickListener() {
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
                loading = ProgressDialog.show(getActivity(),
                        "Mengambil Data","Harap Menunggu...",
                        false,false);
            }

            @Override
            protected String doInBackground(Void... voids) { // saat proses
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(ConfigurationPortofolio.URL_GET_USER,myStr);
                return result;
            }

            @Override
            protected void onPostExecute(String message) { // setelah proses
                super.onPostExecute(message);
                loading.dismiss();
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

            String nama = object.getString(ConfigurationPortofolio.KEY_LOG_NAMA);
            String balance = object.getString(ConfigurationPortofolio.KEY_LOG_BALANCE);
            String total = object.getString(ConfigurationPortofolio.KEY_LOG_TOTAL);
            sendBalance = object.getString(ConfigurationPortofolio.KEY_LOG_TOTAL);

            binding.txtRNama.setText(nama);
            binding.txtRSaldo.setText("Rp. "+balance);
            binding.txtRTotal.setText("Rp. "+total);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
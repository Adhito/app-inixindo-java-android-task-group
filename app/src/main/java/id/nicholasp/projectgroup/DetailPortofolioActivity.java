package id.nicholasp.projectgroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import id.nicholasp.projectgroup.databinding.ActivityDetailPortofolioBinding;
import id.nicholasp.projectgroup.databinding.ActivityLoginFormBinding;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DetailPortofolioActivity extends AppCompatActivity {
    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";
    // key for storing email.
    public static final String USER_KEY = "user_key";
    // variable for shared preferences.
    SharedPreferences sharedpreferences;

    String myStr,JSON_STRING,getBalance;
    ActivityDetailPortofolioBinding binding;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_portofolio);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        myStr = sharedpreferences.getString(USER_KEY, null);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        getBalance = extras.getString("keyBalance");

        binding.txtDBalance.setText(formatRupiah(Double.parseDouble(getBalance)));
        getJSON();

    }

    private String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void getJSON() {
        // batuan dari class AsynTask
        class GetJSON extends AsyncTask<Void,Void,String> { // boleh membuat class dalam method (Inner Class)
            ProgressDialog loading;

            @Override
            protected void onPreExecute() { // sebelum proses
                super.onPreExecute();
                loading = ProgressDialog.show(DetailPortofolioActivity.this,
                        "Mengambil Data","Harap Menunggu...",
                        false,false);
            }

            @Override
            protected String doInBackground(Void... voids) { // saat proses
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(ConfigurationPortofolio.URL_GET_DETAIL_PORTOFOLIO,myStr);
                Log.d("result: ", result);
                return result;
            }

            @Override
            protected void onPostExecute(String message) { // setelah proses
                super.onPostExecute(message);
                loading.dismiss();
                JSON_STRING = message;

                displayDetailData();
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void displayDetailData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(ConfigurationPortofolio.TAG_JSON_ARRAY);
            Log.d("DATA JSON: ", JSON_STRING);
            //Toast.makeText(getActivity(), "DATA JSON" + JSON_STRING, Toast.LENGTH_SHORT).show();

            for (int i = 0; i < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);
                String seri = object.getString(ConfigurationPortofolio.KEY_LOG_SERI);
                String produk = object.getString(ConfigurationPortofolio.KEY_LOG_NAMA_PRODUK);
                String total = object.getString(ConfigurationPortofolio.KEY_LOG_TOTAL);
                String harga = object.getString(ConfigurationPortofolio.KEY_LOG_HARGA_BELI);
                Integer harga_total = Integer.parseInt(total) * Integer.parseInt(harga);

                HashMap<String, String> portofolio = new HashMap<>();
                portofolio.put(ConfigurationPortofolio.TAG_JSON_SERI, seri);
                portofolio.put(ConfigurationPortofolio.TAG_JSON_PRODUK, produk);
                portofolio.put(ConfigurationPortofolio.TAG_JSON_TOTAL, total);
                portofolio.put(ConfigurationPortofolio.TAG_JSON_BELI, formatRupiah(Double.parseDouble(harga_total.toString())));

                //ubah format JSON menjadi Array List
                list.add(portofolio);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // adapter untuk meletakan array list kedalam list view

        ListAdapter adapter = new SimpleAdapter(
                DetailPortofolioActivity.this, list,
                R.layout.list_detail_portofolio,
                new String[]{ConfigurationPortofolio.TAG_JSON_SERI, ConfigurationPortofolio.TAG_JSON_PRODUK,
                ConfigurationPortofolio.TAG_JSON_TOTAL,ConfigurationPortofolio.TAG_JSON_BELI},
                new int[]{R.id.txt_r_seri, R.id.txt_r_prdk,R.id.txt_r_unit, R.id.txt_r_harga}
        );
        binding.lvDtlPortofolio.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
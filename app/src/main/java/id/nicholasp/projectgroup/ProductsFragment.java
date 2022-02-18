package id.nicholasp.projectgroup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProductsFragment extends Fragment {
    private ProgressDialog loading;
    String JSON_STRING, search_txt;
    ListView listview;
    Button btn_beli;
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        listview = view.findViewById(R.id.listViewProduct);
        search = view.findViewById(R.id.txt_search);
        getJsonData();

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                search_txt = search.getText().toString();
                getJsonSearchData(search_txt);
            }});

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), ProductDetailActivity.class);
                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
                String id_product = map.get(Configuration.TAG_JSON_ID_PRODUCT).toString();
                myIntent.putExtra(Configuration.PGW_ID, id_product);
                Log.d("Clicked Product No :", id_product);
                startActivity(myIntent);
            }

        });

//        View view2 = inflater.inflate(R.layout.list_produk_layout, container, false);
//        btn_beli = view2.findViewById(R.id.button);
//        btn_beli.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Test List View", Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }


    public void getJsonData() {
        class GetJsonData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getContext(), "Ambil Data Instruktur", "Harap menunggu...", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(Configuration.GET_ALL_PRODUCT);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                JSON_STRING = message;
                Log.d("DATA_JSON: ", JSON_STRING);

                // menampilkan data json kedalam list view
                displayAllData();
            }
        }
        GetJsonData getJsonData = new GetJsonData();
        getJsonData.execute();
    }

    public void getJsonSearchData(String str) {
        class GetJsonSearchData extends AsyncTask<Void, Void, String> {
            String str_ct = "%" + str + "%";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getContext(), "Ambil Data Instruktur", "Harap menunggu...", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(Configuration.URL_GET_ALL_PRODUCT_SEARCH, str_ct);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                JSON_STRING = message;
                Log.d("DATA_JSON: ", JSON_STRING);

                // menampilkan data json kedalam list view
                displayAllData();
            }
        }
        GetJsonSearchData getJsonSearchData = new GetJsonSearchData();
        getJsonSearchData.execute();
    }

    private void displayAllData() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String id_produk = object.getString("id_produk");
                String seri_produk = object.getString("seri_produk");
                String yield = object.getString("yield");
                String jatuh_tempo = object.getString("jatuh_tempo");
                String nilai_unit = object.getString("nilai_unit");

                HashMap<String, String> produk = new HashMap<>();
                produk.put("id_produk", id_produk);
                produk.put("seri_produk", seri_produk);
                produk.put("yield", yield + "%");
                produk.put("jatuh_tempo", jatuh_tempo);
                produk.put("nilai_unit", formatRupiah(Double.parseDouble(nilai_unit)));
                list.add(produk);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // adapter untuk meletakkan array list kedalam list view
        ListAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.list_produk_layout,
                new String[]{"id_produk","seri_produk", "jatuh_tempo", "yield", "nilai_unit"},
                new int[]{R.id.txt_id_produk, R.id.txt_nama_produk, R.id.txt_jatuh_tempo, R.id.txt_yield, R.id.txt_nilai_unit}
        );

        listview.setAdapter(adapter);


    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}
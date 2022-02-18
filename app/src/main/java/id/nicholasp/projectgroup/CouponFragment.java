package id.nicholasp.projectgroup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class CouponFragment extends Fragment {
    // Session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String ID_KEY = "id_key";

    private ProgressDialog loading;
    private String JSON_STRING;
    String id_detail_user, search_txt;
    ListView listview;
    SharedPreferences sharedpreferences;
    EditText search;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public CouponFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CouponFragment newInstance(String param1, String param2) {
        CouponFragment fragment = new CouponFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);

        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_detail_user = sharedpreferences.getString(ID_KEY, null);

        listview = view.findViewById(R.id.listViewCoupon);
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
//                Intent myIntent = new Intent(getActivity(), ProductDetailActivity.class);
//                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
//                String id_product = map.get(Configuration.TAG_JSON_ID_PRODUCT).toString();
//                myIntent.putExtra(Configuration.PGW_ID, id_product);
//                Log.d("Clicked Product No :", id_product);
//                startActivity(myIntent);
            }

        });

        return view;
    }


    public void getJsonData() {

        class GetJsonData extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                String result = handler.sendGetResponse(Configuration.URL_GET_ALL_COUPON, id_detail_user);
                return result;
            }

            @Override
            protected void onPostExecute(String message) {
                super.onPostExecute(message);
                JSON_STRING = message;
                Log.d("DATA_JSON: ", JSON_STRING);
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
                String result = handler.sendGetRespCoupon(Configuration.URL_GET_ALL_COUPON_SEARCH, id_detail_user, str_ct);
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
                String seri_produk = object.getString("seri_produk");
                String kupon = object.getString("kupon");
                String tgl_kupon = object.getString("tgl_kupon");

                HashMap<String, String> coupon = new HashMap<>();
                coupon.put("seri_produk", seri_produk);
                coupon.put("kupon", formatRupiah(Double.parseDouble(kupon)));
                coupon.put("tgl_kupon", tgl_kupon);
                list.add(coupon);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // adapter untuk meletakkan array list kedalam list view
        ListAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.list_kupon,
                new String[]{"seri_produk","kupon", "tgl_kupon"},
                new int[]{R.id.txt_seri_produk, R.id.txt_kupon, R.id.txt_tgl_kupon}
        );

        listview.setAdapter(adapter);


    }
    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

}
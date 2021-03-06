package id.nicholasp.projectgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    // Session
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_KEY = "user_key";
    SharedPreferences sharedpreferences;
    int fid = 0;
    int his = 0;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // kita set default nya Home Fragment
        getSupportActionBar().setTitle("Portofolio");
        loadFragment(new PortofolioFragment(), 0);
        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_nav_items, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.toolbar_menu_exit:
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.clear();
//                editor.apply();
//                startActivity(new Intent(this, LoginFormActivity.class));
//                Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_LONG).show();
//                break;
//            default:
//                Toast.makeText(this, "No Menu is selected", Toast.LENGTH_LONG).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.nav_portofolio:
                fid = 0;
                getSupportActionBar().setTitle("Portofolio");
                fragment = new PortofolioFragment();
                break;
            case R.id.nav_profile:
                fid = 1;
                getSupportActionBar().setTitle("Profile");
                fragment = new ProfileFragment();
                break;
            case R.id.nav_product:
                fid = 2;
                getSupportActionBar().setTitle("Produk");
                fragment = new ProductsFragment();
                break;
            case R.id.nav_coupon:
                fid = 3;
                getSupportActionBar().setTitle("Coupon");
                fragment = new CouponFragment();
                break;
            case R.id.nav_more:
                fid = 4;
                getSupportActionBar().setTitle("More");
                fragment = new MoreFragment();
                break;
        }
        return loadFragment(fragment, fid);
    }

    private boolean loadFragment(Fragment fragment, Integer in){
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            if (his < in) {
                transaction.setCustomAnimations(R.anim.anim_left, R.anim.anim_static);
                Log.d("Frag", "loadFragmentIn: " + fid);
                Log.d("Frag", "loadFragmentHis: " + his);
            }
            if (his > in){
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                Log.d("Frag", "loadFragmentIn: " + fid);
                Log.d("Frag", "loadFragmentHis: " + his);
            }
            transaction.replace(R.id.fl_container, fragment, String.valueOf(in));
            his = in;
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }
}
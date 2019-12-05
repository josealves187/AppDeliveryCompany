package br.com.jose.alves.freedeliverycliente.Activity.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import br.com.jose.alves.freedeliverycliente.Activity.ReceiveMerchandiseActivity;
import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.HomeViewPagerAdapter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener   {


    private BottomNavigationView mbnvMenu;
    private ViewPager mVpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        mbnvMenu.setOnNavigationItemSelectedListener(this);
        initViewPager();

    }
    private void initComponents() {
        mbnvMenu = findViewById(R.id.bnv_menu);
        mVpContent = findViewById(R.id.vp_content);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_qrcode:
                mVpContent.setCurrentItem(0, false);
                return true;

            case R.id.nav_anucio:
                mVpContent.setCurrentItem(1, false);
                return true;

            case R.id.nav_endereco:
                mVpContent.setCurrentItem(2, false);
                return true;

            case R.id.nav_pefil:
                mVpContent.setCurrentItem(3, false);
                return true;
        }
        return false;
    }

    private void initViewPager() {
        mVpContent.setAdapter(new HomeViewPagerAdapter(getSupportFragmentManager()));
        mVpContent.setCurrentItem(0, false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("PARKOK", MODE_PRIVATE);
        int screenOrigin = preferences.getInt("SCREEN_ORIGEN", 0);

        switch (screenOrigin) {
            case 1:
                mVpContent.setCurrentItem(0, false);
                break;

            case 2:
                mVpContent.setCurrentItem(1, false);
                break;

            case 3:
                mVpContent.setCurrentItem(2, false);
                break;

            case 4:
                mVpContent.setCurrentItem(3, false);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null){
            if (intentResult.getContents() !=  null){

                alert(intentResult.getContents());

                Intent i = new Intent(MainActivity.this, ReceiveMerchandiseActivity.class);
                i.putExtra("ITEMPURCHASES", intentResult.getContents());
                startActivity(i);

            }else{
                Toast.makeText(this,"Scan cancelado",Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    private void alert(String msg){
    }


}

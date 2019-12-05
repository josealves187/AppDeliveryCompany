package br.com.jose.alves.freedeliverycliente.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.ViewPagerAdapter;
import br.com.jose.alves.freedeliverycliente.view.ViewPagerCustom;

public class DeliveryReceivedActivity extends AppCompatActivity {

//

    private TabLayout tlReferFriend;
    private ViewPagerCustom vpView;
    private MaterialToolbar mt_toolbar_refer_friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_received);
        initializeFindViewById();
        setupTabLayout();

    }

    private void setupTabLayout() {
        tlReferFriend = this.findViewById(R.id.tl_refer_friend);
        vpView = this.findViewById(R.id.vp_view);


        tlReferFriend.addTab(tlReferFriend.newTab().setText(getString(R.string.refer_friend)));
        tlReferFriend.addTab(tlReferFriend.newTab().setText(getString(R.string.referpack)));
        vpView.setOffscreenPageLimit(2);
        vpView.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        tlReferFriend.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(
                        vpView
                )
        );

        vpView.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(
                        tlReferFriend
                )
        );
    }


    private void initializeFindViewById() {
        tlReferFriend = findViewById(R.id.tl_refer_friend);
        vpView = findViewById(R.id.vp_view);
        mt_toolbar_refer_friends = findViewById(R.id.mt_toolbar_refer_friends);
    }


}

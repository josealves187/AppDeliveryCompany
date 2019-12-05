package br.com.jose.alves.freedeliverycliente.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.ViewPagerAdapter;
import br.com.jose.alves.freedeliverycliente.view.ViewPagerCustom;

public class AnucioFragment extends Fragment {


    private TabLayout tlReferFriend;
    private ViewPagerCustom vpView;
    private MaterialToolbar mt_toolbar_refer_friends;

    public AnucioFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anucio, container, false);

        initializeFindViewById(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupTabLayout();
    }

    private void setupTabLayout() {
        tlReferFriend = getActivity().findViewById(R.id.tl_refer_friend);
        vpView = getActivity().findViewById(R.id.vp_view);


        tlReferFriend.addTab(tlReferFriend.newTab().setText(getString(R.string.refer_friend)));
        tlReferFriend.addTab(tlReferFriend.newTab().setText(getString(R.string.referpack)));
        vpView.setOffscreenPageLimit(2);
        vpView.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

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


    private void initializeFindViewById( View view) {
        tlReferFriend = view.findViewById(R.id.tl_refer_friend);
        vpView = view.findViewById(R.id.vp_view);
        mt_toolbar_refer_friends = view.findViewById(R.id.mt_toolbar_refer_friends);
    }


}


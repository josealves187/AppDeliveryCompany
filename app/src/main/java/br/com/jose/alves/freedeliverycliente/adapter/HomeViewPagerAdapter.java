package br.com.jose.alves.freedeliverycliente.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import br.com.jose.alves.freedeliverycliente.fragment.AnucioFragment;
import br.com.jose.alves.freedeliverycliente.fragment.EndecoFragment;
import br.com.jose.alves.freedeliverycliente.fragment.PefilFragment;
import br.com.jose.alves.freedeliverycliente.fragment.QRCodeFragment;


public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    public HomeViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new QRCodeFragment();
            case 1:
                return new AnucioFragment();
            case 2:
                return new EndecoFragment();
            case 3:
                return new PefilFragment();

        }
        return new QRCodeFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }
}

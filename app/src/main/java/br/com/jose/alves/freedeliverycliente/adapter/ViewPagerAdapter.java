package br.com.jose.alves.freedeliverycliente.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import br.com.jose.alves.freedeliverycliente.fragment.ProductsConfirmedFragment;
import br.com.jose.alves.freedeliverycliente.fragment.UnverifiedProductsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProductsConfirmedFragment();

            case 1:
                return new UnverifiedProductsFragment();

            default:
                return new ProductsConfirmedFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

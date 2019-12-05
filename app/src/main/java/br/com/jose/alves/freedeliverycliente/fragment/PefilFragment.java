package br.com.jose.alves.freedeliverycliente.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import br.com.jose.alves.freedeliverycliente.Activity.RegistrationDataActivity;
import br.com.jose.alves.freedeliverycliente.Activity.LoginActivity;
import br.com.jose.alves.freedeliverycliente.Activity.SimpleSlideActivity;
import br.com.jose.alves.freedeliverycliente.Activity.SobreActivity;
import br.com.jose.alves.freedeliverycliente.Activity.UserAddress;
import br.com.jose.alves.freedeliverycliente.R;


public class PefilFragment extends Fragment implements View.OnClickListener{

    private TextView tvRegistrationDat;
    private TextView tvTitleExitProfile;
    private TextView tv_title_contact_us;
    private TextView tv_title_about_the_app;


    public PefilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pefil, container, false);

        InitializedComponents(view);
        implementationInstance();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_registration_dat:
                flameRegistration();
                break;

            case R.id.tv_title_contact_us:
                flameRegistrati();
                break;

            case R.id.tv_title_about_the_app:
                flameSimpleSlideActivity();
                break;

            case R.id.tv_title_exit_profile:
                desconetarUsuario();
                break;

        }

    }

    private void implementationInstance() {
        tvRegistrationDat.setOnClickListener(this);
        tvTitleExitProfile.setOnClickListener(this);
        tv_title_contact_us.setOnClickListener(this);
        tv_title_about_the_app.setOnClickListener(this);
    }

    private void InitializedComponents(View view) {

        tvRegistrationDat = view.findViewById(R.id.tv_registration_dat);
        tvTitleExitProfile = view.findViewById(R.id.tv_title_exit_profile);
        tv_title_contact_us = view.findViewById(R.id.tv_title_contact_us);
        tv_title_about_the_app = view.findViewById(R.id.tv_title_about_the_app);
    }

    private void flameRegistration(){
        Intent i = new Intent(getActivity(), RegistrationDataActivity.class);
        startActivity(i);
    }

    private void flameRegistrati(){
        Intent i = new Intent(getActivity(), UserAddress.class);
        startActivity(i);
    }

    private void flameSimpleSlideActivity(){
        Intent i = new Intent(getActivity(), SobreActivity.class);
        startActivity(i);
    }

    private void desconetarUsuario() {
        try {

            FirebaseAuth.getInstance().signOut();
            chamalogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void chamalogin() {
        Toast.makeText(getActivity(),
                "Usuário Desconectado!",Toast.LENGTH_LONG).show();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

}

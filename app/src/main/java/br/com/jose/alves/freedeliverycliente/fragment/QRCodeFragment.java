package br.com.jose.alves.freedeliverycliente.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;

import br.com.jose.alves.freedeliverycliente.R;


public class QRCodeFragment extends Fragment {

    private CardView cmInputQrcode;


    public QRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        InitializedComponents(view);
        eventButton();

        return view;
    }

    private void InitializedComponents(View view) {
        cmInputQrcode = view.findViewById(R.id.cm_input_qrcode);
    }

    private void eventButton() {
        cmInputQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator((Activity) getContext());
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Alinhar código de barra com QR code para ser lido");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
                IntentIntegrator.forSupportFragment(QRCodeFragment.this).initiateScan();


            }
        });
    }


}

package br.com.jose.alves.freedeliverycliente.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import br.com.jose.alves.freedeliverycliente.Activity.Home.MainActivity;
import br.com.jose.alves.freedeliverycliente.R;

/**

 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Erro de autenticação de impressão digital\n" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Ajuda para autenticação de impressão digital\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Falha na autenticação da impressão digital.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        ((Activity) context).finish();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void update(String e) {
        TextView textView = ((Activity) context).findViewById(R.id.errorText);
        textView.setText(e);
    }

}

package br.com.jose.alves.freedeliverycliente.util.Validation;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class StandardValidation implements Validator {

    private static final String CAMPO_OBRIGATORIO = "Campo obrigatório";
    private final TextInputLayout textInputCampo;
    private final EditText campo;


    public StandardValidation(TextInputLayout textInputCampo) {
        this.textInputCampo = textInputCampo;
        this.campo = this.textInputCampo.getEditText();
    }

    private boolean validaCampoObrigatorio() {
        String texto = campo.getText().toString();
        if (texto.isEmpty()) {
            textInputCampo.setError(CAMPO_OBRIGATORIO);
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid() {
        if(!validaCampoObrigatorio()) return false;
        removeErro();
        return true;
    }


    private void removeErro() {
        textInputCampo.setError(null);
        textInputCampo.setErrorEnabled(false);
    }


}

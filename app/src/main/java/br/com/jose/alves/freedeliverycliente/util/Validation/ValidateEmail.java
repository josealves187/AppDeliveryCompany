package br.com.jose.alves.freedeliverycliente.util.Validation;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class ValidateEmail implements Validator {

    private final TextInputLayout textInputEmail;
    private final EditText campoEmail;
    private final StandardValidation validadorPadrao;

    public ValidateEmail(TextInputLayout textInputEmail) {
        this.textInputEmail = textInputEmail;
        this.campoEmail = this.textInputEmail.getEditText();
        this.validadorPadrao = new StandardValidation(this.textInputEmail);
    }

    private boolean validaPadrao(String email) {
        if (email.matches(".+@.+\\..+")) {
            return true;
        }
        textInputEmail.setError("E-mail inválido");
        return false;
    }


    @Override
    public boolean isValid() {
        if (!validadorPadrao.isValid()) return false;
        String email = campoEmail.getText().toString();
        if (!validaPadrao(email)) return false;
        return true;
    }
}
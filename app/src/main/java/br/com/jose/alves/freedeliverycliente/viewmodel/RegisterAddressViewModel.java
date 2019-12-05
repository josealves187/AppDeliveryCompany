package br.com.jose.alves.freedeliverycliente.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.com.jose.alves.freedeliverycliente.model.Address;
import br.com.jose.alves.freedeliverycliente.network.ApiClient;
import br.com.jose.alves.freedeliverycliente.network.services.ViaCepService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterAddressViewModel extends ViewModel {

    public MutableLiveData<Address> getAddress(String cep) {
        final MutableLiveData<Address> result = new MutableLiveData<>();
        ApiClient.create(ViaCepService.class)
                .getDataAddress(cep)
                .enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        if (response.isSuccessful()) {
                            result.setValue(response.body());
                        } else result.setValue(null);
                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        result.setValue(null);
                    }
                });
        return result;
    }
}


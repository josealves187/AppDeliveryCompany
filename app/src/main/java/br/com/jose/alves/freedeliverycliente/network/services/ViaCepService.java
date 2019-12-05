package br.com.jose.alves.freedeliverycliente.network.services;

import br.com.jose.alves.freedeliverycliente.model.Address;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface  ViaCepService {
    @GET("{cep}/json/")
    public Call<Address> getDataAddress(@Path("cep") String cep);


}

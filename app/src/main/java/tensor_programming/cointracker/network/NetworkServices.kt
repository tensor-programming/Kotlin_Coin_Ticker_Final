package tensor_programming.cointracker.network

import io.reactivex.Flowable
import retrofit2.http.GET
import tensor_programming.cointracker.model.CurrencyModel

/**
 * Created by Tensor on 2/6/2018.
 */
interface NetworkServices {

    @GET("v1/ticker/?convert=IDR&limit=15")
    fun getAllCurrency(): Flowable<List<CurrencyModel>>

}
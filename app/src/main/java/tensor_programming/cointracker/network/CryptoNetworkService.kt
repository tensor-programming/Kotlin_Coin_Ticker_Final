package tensor_programming.cointracker.network

import io.reactivex.disposables.Disposable
import tensor_programming.cointracker.extensions.uiSubscribe
import tensor_programming.cointracker.model.CurrencyModel

/**
 * Created by Tensor on 2/6/2018.
 */
class CryptoNetworkService(private val networkServices: NetworkServices) {

    fun getAllCurrency(onSuccess: (List<CurrencyModel>) -> Unit,
                       onError: (Throwable) -> Unit): Disposable {
        return networkServices.getAllCurrency()
                .uiSubscribe(
                        onNext = { onSuccess(it) },
                        onError = { onError(it) }
                )
    }
}
package com.marinaruiz.facturas_fct.data.network

import android.util.Log
import com.marinaruiz.facturas_fct.core.utils.ENVIRONMENT
import com.marinaruiz.facturas_fct.data.network.invoicesApi.InvoicesApiService
import com.marinaruiz.facturas_fct.data.network.invoicesApi.models.InvoiceApiModel
import com.marinaruiz.facturas_fct.data.network.invoicesApi.models.SSDetailApiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val service: InvoicesApiService
) {
    private val _invoices: MutableStateFlow<List<InvoiceApiModel>?> = MutableStateFlow(null)
    val invoices: StateFlow<List<InvoiceApiModel>?>
        get() = _invoices

    private val _details: MutableStateFlow<SSDetailApiModel?> = MutableStateFlow(null)
    val details: StateFlow<SSDetailApiModel?>
        get() = _details

    companion object {
        private const val TAG = "VIEWNEXT NetworkRepository"
    }

    suspend fun fetchInvoices(environment: String = ENVIRONMENT) {
        try {
            val response = service.fetchAllInvoicesFromApi(environment)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, response.body().toString())
                _invoices.value = response.body()!!.getInvoicesList().map { it.asApiModel() }
            } else {
                Log.e(TAG, response.message())
                _invoices.value = emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            _invoices.value = null
        }

    }

    suspend fun fetchSmartSolarDetails(environment: String = ENVIRONMENT) {
        try {
            val response = service.fetchSmartSolarDetailsFromApi(environment)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, response.body().toString())
                _details.value = response.body()!!.asSSDetailApiModel()
            } else {
                Log.e(TAG, response.message())
                _details.value = SSDetailApiModel()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            _details.value = null
        }

    }
}
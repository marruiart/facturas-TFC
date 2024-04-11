package com.example.facturas.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.facturas.data.network.invoicesApi.InvoicesApiService
import com.example.facturas.data.network.invoicesApi.models.InvoiceApiModel
import com.example.facturas.utils.ENVIRONMENT

class NetworkRepository private constructor(
    private val service: InvoicesApiService
) {
    private val _invoices: MutableLiveData<List<InvoiceApiModel>> = MutableLiveData(emptyList())
    val invoices: LiveData<List<InvoiceApiModel>>
        get() = _invoices

    companion object {
        private var _INSTANCE: NetworkRepository? = null

        fun getInstance(): NetworkRepository {
            return _INSTANCE ?: NetworkRepository(InvoicesApiService.getInstance())
        }
    }

    suspend fun fetchInvoices(environment: String = ENVIRONMENT) {
        try {
            val response = service.getAllInvoices()
            if (response.isSuccessful && response.body() != null) {
                Log.d("DEBUG INVOICES RESPONSE", response.body().toString())
                _invoices.postValue(response.body()!!.getInvoicesList().map { it.asApiModel() })
            } else {
                Log.e("ERROR", response.message())
                _invoices.postValue(emptyList())
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            _invoices.postValue(emptyList())
        }

    }
}
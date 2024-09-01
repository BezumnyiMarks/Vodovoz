package com.example.vodovoz.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vodovoz.Data.Products
import com.example.vodovoz.Repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val products = MutableStateFlow(Products("", "", listOf()))
    val productsLoading = MutableStateFlow(false)
    private val productsError = MutableStateFlow(false)
    val category = MutableStateFlow("")
    val tabPosition = MutableStateFlow(0)

    fun loadProducts(){
        productsLoading.value = true
        productsError.value = false
        viewModelScope.launch {
            kotlin.runCatching {
                Repository.RetrofitInstance.searchProducts.getProducts()
            }.fold(
                onSuccess = {
                    products.value = it
                    productsLoading.value = false
                    productsError.value = false
                },
                onFailure = {
                    Log.d("LoadProducts", it.message ?:"")
                    productsLoading.value = false
                    productsError.value = true
                }
            )
        }
    }
}
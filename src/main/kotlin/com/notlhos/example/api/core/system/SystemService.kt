package com.notlhos.example.api.core.system

import com.notlhos.example.api.bean.info.CurrencyInfo

interface SystemService {
    suspend fun getRates(): Collection<CurrencyInfo>
}
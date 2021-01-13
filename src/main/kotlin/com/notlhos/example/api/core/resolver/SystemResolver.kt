package com.notlhos.example.api.core.resolver

import com.notlhos.example.api.core.info.CurrencyInfo
import java.time.Instant

interface SystemResolver {
    fun getMiddleRates(): Collection<CurrencyInfo>
    fun getMiddleRatesByRange(start: Long, end: Long = Instant.now().toEpochMilli()): Collection<CurrencyInfo>
}
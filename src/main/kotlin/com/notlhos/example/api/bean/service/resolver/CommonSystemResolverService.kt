package com.notlhos.example.api.bean.service.resolver

import com.notlhos.example.api.core.info.CurrencyInfo
import com.notlhos.example.api.core.resolver.SystemResolver
import com.notlhos.example.api.bean.repository.CurrencyInfoRepository
import org.springframework.stereotype.Service

@Service
class CommonSystemResolverService(private val currencyRepository: CurrencyInfoRepository) : SystemResolver {

    override fun getMiddleRates(): Collection<CurrencyInfo> {
        return currencyRepository.getMiddleRates().toHashSet()
    }

    override fun getMiddleRatesByRange(start: Long, end: Long): Collection<CurrencyInfo> {
        return currencyRepository.getMiddleRatesByRange(start, end).toHashSet()
    }

}
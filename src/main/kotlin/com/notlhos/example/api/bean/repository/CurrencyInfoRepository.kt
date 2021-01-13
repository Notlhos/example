package com.notlhos.example.api.bean.repository

import com.notlhos.example.api.core.repository.CurrencyRepository
import com.notlhos.example.api.bean.info.CurrencyInfo
import org.springframework.stereotype.Repository

@Repository
interface CurrencyInfoRepository : CurrencyRepository<CurrencyInfo> {
}
package com.notlhos.example.api.bean.info

import com.notlhos.example.api.core.info.CurrencyInfo

data class SimpleCurrencyInfo(override val code: String, override val buy: Double, override val sell: Double) : CurrencyInfo{
}
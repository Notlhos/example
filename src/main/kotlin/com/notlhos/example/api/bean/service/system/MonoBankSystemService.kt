package com.notlhos.example.api.bean.service.system

import com.fasterxml.jackson.module.kotlin.readValue
import com.notlhos.example.api.core.simplifiable.Simplifiable
import com.notlhos.example.api.core.system.SystemService
import com.notlhos.example.api.bean.info.CurrencyInfo
import com.notlhos.example.api.util.JacksonAPI
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

@Component
class MonoBankSystemService : SystemService {

    override suspend fun getRates() : Collection<CurrencyInfo> {
        val url = URL(CURRENCY_URL)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connect()

        if (connection.responseCode != 200) {
            throw IOException("Can't get response from MonoBank")
        }

        val content = connection.content as? InputStream
            ?: throw IOException("Can't get response from MonoBank by content")

        val currencies = JacksonAPI.objectMapper.readValue<Collection<MonoBankCurrencyInfo>>(content)

        return currencies.mapNotNull(MonoBankCurrencyInfo::simplify)
    }

    companion object {
        const val BANK = "MONOBANK"
        const val CURRENCY_URL = "https://api.monobank.ua/bank/currency"
    }

    private class MonoBankCurrencyInfo(val currencyCodeA: Int, val date: Long, val rateSell: Double = 0.0, val rateBuy: Double = 0.0) : Simplifiable<CurrencyInfo?> {

        override fun simplify(): CurrencyInfo? {

            if(rateSell <= 0.0 || rateBuy <= 0.0) return null

            val currencyCode = Currency.getAvailableCurrencies()
                .find { currency -> currency.numericCode == currencyCodeA }
                ?.currencyCode ?: return null

            val id = UUID.nameUUIDFromBytes("${currencyCode}_$BANK".toByteArray())



            return CurrencyInfo(id, currencyCode, date, rateBuy, rateSell)
        }

    }

}
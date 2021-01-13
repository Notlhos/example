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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

@Component
class PrivatBankSystemService : SystemService {

    override suspend fun getRates() : Collection<CurrencyInfo> {
        val instant = Instant.now()
        val currencies = ArrayList<CurrencyInfo>(806)

        for(range in 0..30){
            val rangeInstant = instant.minus(range.toLong(), ChronoUnit.DAYS)
            val rangeDate = Date.from(rangeInstant)

            val url = URL(CURRENCY_URL + formatter.format(rangeDate))
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.connect()

            val content = connection.content as? InputStream
                ?: throw IOException("Can't get response from PrivatBank by content")

            currencies.addAll(JacksonAPI.objectMapper.readValue<PrivatBankResponseInfo>(content).simplify())
        }


        return currencies
    }

    companion object {
        const val BANK = "PRIVATBANK"
        const val CURRENCY_URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date="

        val formatter = SimpleDateFormat("dd.MM.yyyy")
    }

    private class PrivatBankResponseInfo(val date: String, val exchangeRate: Collection<PrivatBankCurrencyInfo>): Simplifiable<Collection<CurrencyInfo>> {

        override fun simplify(): Collection<CurrencyInfo> {
            val dateTime = formatter.parse(date).time

            return exchangeRate.filter { it.currency.isNotEmpty() }.map {
                val currencyCode = it.currency
                val id = UUID.nameUUIDFromBytes("${currencyCode}_${BANK}_$dateTime".toByteArray())
                CurrencyInfo(id, currencyCode, dateTime, it.purchaseRateNB, it.saleRateNB)
            }
        }

    }

    private class PrivatBankCurrencyInfo(val currency: String = "", val saleRateNB: Double, val purchaseRateNB: Double)

}
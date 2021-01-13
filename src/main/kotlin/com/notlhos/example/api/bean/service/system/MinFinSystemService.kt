package com.notlhos.example.api.bean.service.system

import com.fasterxml.jackson.module.kotlin.readValue
import com.notlhos.example.api.core.simplifiable.Simplifiable
import com.notlhos.example.api.core.system.SystemService
import com.notlhos.example.api.bean.info.CurrencyInfo
import com.notlhos.example.api.util.JacksonAPI
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant
import java.util.*

@Component
class MinFinSystemService : SystemService {

    @Value("\${minfin.key}")
    private lateinit var key: String

    override suspend fun getRates(): Collection<CurrencyInfo> {
        val url = URL(CURRENCY_URL + key)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connect()

        if (connection.responseCode != 200) {
            throw IOException("Can't get response from MinFin")
        }

        val content = connection.content as? InputStream
            ?: throw IOException("Can't get response from MinFin by content")

        val currencies = JacksonAPI.objectMapper.readValue<Collection<MinFinCurrencyInfo>>(content)

        return currencies.mapNotNull(MinFinCurrencyInfo::simplify)
    }

    companion object {
        const val BANK = "MINFIN"
        const val CURRENCY_URL = "https://api.minfin.com.ua/mb/"
    }

    private class MinFinCurrencyInfo(val ask: String, val bid: String, val currency: String) :
        Simplifiable<CurrencyInfo> {

        override fun simplify(): CurrencyInfo {
            val currencyCode = currency.toUpperCase()
            val id = UUID.nameUUIDFromBytes("${currencyCode}_$BANK".toByteArray())



            return CurrencyInfo(id, currencyCode, Instant.now().toEpochMilli(), ask.toDouble(), bid.toDouble())
        }

    }

}
package com.notlhos.example.api.core.repository

import com.notlhos.example.api.core.info.CurrencyInfo
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param
import java.util.*

@NoRepositoryBean
interface CurrencyRepository<P : CurrencyInfo> : CrudRepository<P, UUID> {

    @Query("select new com.notlhos.example.api.bean.info.SimpleCurrencyInfo(ci.code, avg(ci.buy), avg(ci.sell)) from CurrencyInfo ci group by ci.code")
    fun getMiddleRates(): Iterable<CurrencyInfo>

    @Query("select new com.notlhos.example.api.bean.info.SimpleCurrencyInfo(ci.code, avg(ci.buy), avg(ci.sell)) from CurrencyInfo ci where ci.date > :from and ci.date < :to group by ci.code")
    fun getMiddleRatesByRange(@Param("from") from: Long, @Param("to") to: Long): Iterable<CurrencyInfo>
}
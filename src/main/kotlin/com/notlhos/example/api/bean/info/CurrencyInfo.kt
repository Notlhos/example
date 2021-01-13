package com.notlhos.example.api.bean.info

import com.notlhos.example.api.core.info.CurrencyInfo
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CurrencyInfo(@Id val uuid: UUID, override val code: String, val date: Long, override val buy: Double, override val sell: Double) : CurrencyInfo{
}
package com.notlhos.example.api.mwc.controller

import com.notlhos.example.api.core.info.CurrencyInfo
import com.notlhos.example.api.core.resolver.SystemResolver
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/currency")
@Api(value = "CurrencyController", description = "Api for current currency rates")
class CurrencyController(private val systemResolver: SystemResolver) {

    @ResponseBody
    @RequestMapping(path = ["/now"], method = [ RequestMethod.GET ])
    fun nowRates(): Collection<CurrencyInfo> {
        return systemResolver.getMiddleRates()
    }

    @ResponseBody
    @RequestMapping(path = ["/range"], method = [ RequestMethod.GET ])
    fun rangeRates(@RequestParam from: Long, @RequestParam(required = false) to: Long?): Collection<CurrencyInfo> {
        return to?.let { systemResolver.getMiddleRatesByRange(from, to) } ?: systemResolver.getMiddleRatesByRange(from)
    }

}
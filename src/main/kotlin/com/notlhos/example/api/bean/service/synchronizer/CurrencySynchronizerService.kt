package com.notlhos.example.api.bean.service.synchronizer

import com.notlhos.example.api.core.synchronizer.Synchronizer
import com.notlhos.example.api.core.system.SystemService
import com.notlhos.example.api.bean.repository.CurrencyInfoRepository
import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@Component
class CurrencySynchronizerService(
    private val services: Set<SystemService>,
    private val currencyRepository: CurrencyInfoRepository
) : Synchronizer {

    private var started: Boolean = false

    @PostConstruct
    @ExperimentalTime
    override fun syncing(): Boolean {
        if (started) return false

        GlobalScope.launch {
            launch {
                for (service in services) {
                    launch {
                        while (true) {
                            async {
                                val job = runCatching { service.getRates() }

                                job.onSuccess { currencyRepository.saveAll(it) }
                                job.onFailure { return@async }

                            }.await()


                            delay(5.toDuration(TimeUnit.MINUTES))
                        }
                    }
                }
            }.join()
        }

        started = true
        return true
    }

}
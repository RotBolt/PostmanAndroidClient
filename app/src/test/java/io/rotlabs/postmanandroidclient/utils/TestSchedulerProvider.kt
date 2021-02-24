package io.rotlabs.postmanandroidclient.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import io.rotlabs.postmanandroidclient.utils.rx.SchedulerProvider

class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider {
    override fun computation(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler

    override fun ui(): Scheduler = testScheduler
}
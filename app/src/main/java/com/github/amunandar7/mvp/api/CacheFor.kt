package com.github.amunandar7.mvp.api

object CacheFor {
    const val KEY = "Cache-For"
    const val ONE_DAY = "$KEY: ${60 * 60 * 24}"
    const val ONE_HOUR = "$KEY: ${60 * 60}"
}
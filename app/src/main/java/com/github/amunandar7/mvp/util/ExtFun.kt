package com.github.amunandar7.mvp.util


inline fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

inline fun String?.notNullOrEmpty(f: (it: String) -> Unit) {
    if (this != null && !this.isEmpty()) f(this)
}
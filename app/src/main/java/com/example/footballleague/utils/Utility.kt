package com.example.footballleague.utils

import okhttp3.RequestBody
import okio.Buffer
import java.io.IOException

fun bodyToString(request: RequestBody?): String? {
    return try {
        val copy: RequestBody? = request
        val buffer = Buffer()
        if (copy != null) copy.writeTo(buffer) else return ""
        buffer.readUtf8()
    } catch (e: IOException) {
        "did not work"
    }
}
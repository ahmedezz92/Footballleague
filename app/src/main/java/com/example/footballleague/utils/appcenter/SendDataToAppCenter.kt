package com.example.footballleague.utils.appcenter

import com.microsoft.appcenter.Flags
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

fun sendCrashData( exception: Exception, properties: HashMap<String?, String?>) {

    properties.put("message",exception.message)
    properties.put("stackTrace",exception.stackTraceToString())

    Crashes.trackError(exception, properties, null)
    //Analytics.trackEvent(exception.message, properties, Flags.CRITICAL);
    sendEventData(exception.message.toString(),properties)
}

fun sendEventData( eventName: String, properties: HashMap<String?, String?>) {
    Analytics.trackEvent(eventName, properties, Flags.CRITICAL);
}

fun sendEventData(className:String
                  , requestUrl:String
                  , requestType:String
                  , isHttps:String
                  , code:String
                  , requestBody: String) {

    val properties: HashMap<String?, String?> = object : HashMap<String?, String?>() {
        init {
            put("Class", className)
            put("RequestUrl", requestUrl)
            put("RequestType",requestType)
            put("isHttps",isHttps)
            put("Code",code)
            if(requestType == "POST")
                put("Body", requestBody)
        }
    }

    Analytics.trackEvent(requestUrl, properties, Flags.CRITICAL);
}
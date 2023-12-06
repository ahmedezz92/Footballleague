package com.example.footballleague.utils.resource

import org.json.JSONObject

fun handleErrorJSON(response: String): String {

    val jObjError = JSONObject(response)
    return jObjError.getString("ResponseMessage");
}



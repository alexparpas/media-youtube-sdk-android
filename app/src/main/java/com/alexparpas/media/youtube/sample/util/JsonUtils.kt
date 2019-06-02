package com.alexparpas.media.youtube.sample.util

import com.google.gson.Gson
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type

/**
 * Parse raw json file to an object from your resources directory.
 *
 * Example call:
 * val matchResponse = TestUtils.fromJson("brighton-leicester.json", MatchResponse::class.java)
 *
 * @param path Path inside your resources that the json file is in.
 * Strongly recommended to use the default value to keep it consistent across all InCrowd apps.
 * @param fileName Name of the json file you want to parse.
 * @param clazz File name of the class you want to parse to.
 */
object JsonUtils {
    inline fun <reified T> fromJson(fileName: String, clazz: Class<T>, path: String = "api-response"): T? {

        return try {
            val inputStream = javaClass.classLoader?.getResourceAsStream("$path/$fileName")
            inputStream?.let {
                val inputStreamReader = InputStreamReader(it, Charsets.UTF_8)
                Gson().fromJson(inputStreamReader, clazz)
            }
        } catch (ex: IOException) {
            null
        }
    }

    inline fun <reified T> fromJson(fileName: String, type: Type, path: String = "api-response"): T? {

        return try {
            val inputStream = javaClass.classLoader?.getResourceAsStream("$path/$fileName")
            inputStream?.let {
                val inputStreamReader = InputStreamReader(it, Charsets.UTF_8)
                Gson().fromJson(inputStreamReader, type)
            }
        } catch (ex: IOException) {
            null
        }
    }
}

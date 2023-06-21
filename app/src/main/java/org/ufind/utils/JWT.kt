package org.ufind.utils

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


class JWT {
    companion object {
        private fun getJson(strEncoded: String): String {
            val decodedBytes: ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
            return String(decodedBytes, Charset.forName("UTF-8"))
        }
        fun decoded(JWTEncoded: String): String {
            return try {
                val split = JWTEncoded.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                getJson(split[1])
            } catch (e: UnsupportedEncodingException) {
                ""
            }
        }
    }
}
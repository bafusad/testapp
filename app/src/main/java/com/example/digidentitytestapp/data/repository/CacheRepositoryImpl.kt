package com.example.digidentitytestapp.data.repository

import android.content.SharedPreferences
import android.util.Base64
import com.example.digidentitytestapp.data.entity.CatalogItem
import com.example.digidentitytestapp.data.mapper.ItemMapper
import com.example.digidentitytestapp.data.security.SecurityUtil
import com.example.digidentitytestapp.domain.entity.Item
import com.example.digidentitytestapp.domain.repository.CacheRepository
import com.google.gson.Gson
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

private const val KEY_CACHE = "items_cache"
private const val KEY_SECRET = "secret_key"
private const val KEY_VECTOR = "vector"

class CacheRepositoryImpl(
    private val gson: Gson,
    private val mapper: ItemMapper,
    private val securityUtil: SecurityUtil,
    private val sharedPreferences: SharedPreferences
) : CacheRepository {

    override fun saveItems(items: List<Item>) {
        val secretKey = sharedPreferences.getString(KEY_SECRET, "")
        val vector = sharedPreferences.getString(KEY_VECTOR, "")

        val catalogItems = items.map(mapper::mapToData)
        val jsonString = gson.toJson(catalogItems)

        if (secretKey != null && vector != null) {
            val encryptedString = securityUtil.encrypt(jsonString, secretKey, vector)
            sharedPreferences.edit().putString(KEY_CACHE, encryptedString).apply()
        }
    }

    override fun getCacheItems(): List<Item> {
        val secretKey = sharedPreferences.getString(KEY_SECRET, "")
        val vector = sharedPreferences.getString(KEY_VECTOR, "")
        val encryptedJson = sharedPreferences.getString(KEY_CACHE, "")

        return if (encryptedJson != null && secretKey != null && vector != null) {
            val jsonString = securityUtil.decrypt(encryptedJson, secretKey, vector)
            val catalogItems = gson
                .fromJson(jsonString, Array<CatalogItem>::class.java)
                .toList()
            catalogItems.map(mapper::mapToDomain)
        } else {
            emptyList()
        }
    }

    override fun isSecretKeyGenerated(): Boolean {
        val secret = sharedPreferences.getString(KEY_SECRET, "")
        return !secret.isNullOrEmpty()
    }

    override fun generateAndSaveKey() {
        val keyGenerator = KeyGenerator.getInstance("AES").apply {
            init(AES_KEY_SIZE)
        }
        val secretKey: SecretKey = keyGenerator.generateKey()
        val vector = ByteArray(VECTOR_BYTE_ARRAY_SIZE)
        SecureRandom().apply { nextBytes(vector) }

        val secretKeyString = Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)
        val vectorString = Base64.encodeToString(vector, Base64.NO_WRAP)
        sharedPreferences.edit().apply {
            putString(KEY_SECRET, secretKeyString)
            putString(KEY_VECTOR, vectorString)
        }.apply()
    }

    companion object {
        private const val VECTOR_BYTE_ARRAY_SIZE = 16
        private const val AES_KEY_SIZE = 256
    }
}
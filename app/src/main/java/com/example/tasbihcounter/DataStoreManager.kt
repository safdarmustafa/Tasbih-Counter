package com.example.tasbihcounter

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.dataStore by preferencesDataStore(name = "tasbih_prefs")

object DataStoreManager {

    // ===============================
    // 🔹 TASBIH COUNTER
    // ===============================

    private fun countKey(dhikr: String) =
        intPreferencesKey("count_$dhikr")

    fun getCount(context: Context, dhikr: String): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[countKey(dhikr)] ?: 0
        }
    }

    suspend fun saveCount(context: Context, dhikr: String, count: Int) {
        context.dataStore.edit { preferences ->
            preferences[countKey(dhikr)] = count
        }
    }

    // ===============================
    // 🔹 PRAYER TRACKER
    // ===============================

    private val DATE_KEY = stringPreferencesKey("saved_date")

    private fun prayerKey(name: String) =
        booleanPreferencesKey("prayer_state_$name")

    fun getPrayerState(context: Context, name: String): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[prayerKey(name)] ?: false
        }
    }

    suspend fun savePrayerState(context: Context, name: String, value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[prayerKey(name)] = value
            prefs[DATE_KEY] = LocalDate.now().toString()
        }
    }

    suspend fun checkAndResetIfNewDay(context: Context) {
        context.dataStore.edit { prefs ->

            val today = LocalDate.now().toString()
            val savedDate = prefs[DATE_KEY]

            if (savedDate != today) {

                // Remove ONLY prayer state keys
                prefs.asMap().keys
                    .filter { it.name.startsWith("prayer_state_") }
                    .forEach { prefs.remove(it) }

                prefs[DATE_KEY] = today
            }
        }
    }

    // ===============================
    // 🔔 AZAN MODE (NEW)
    // ===============================

    private val AZAN_MODE_KEY = stringPreferencesKey("azan_mode")

    suspend fun saveAzanMode(context: Context, mode: AzanMode) {
        context.dataStore.edit {
            it[AZAN_MODE_KEY] = mode.name
        }
    }

    fun getAzanMode(context: Context): Flow<AzanMode> {
        return context.dataStore.data.map {
            AzanMode.valueOf(
                it[AZAN_MODE_KEY] ?: AzanMode.SILENT.name
            )
        }
    }
}
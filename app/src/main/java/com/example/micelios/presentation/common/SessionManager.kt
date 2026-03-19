package com.example.micelios.presentation.common

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveCurrentUserId(userId: Long) {
        prefs.edit()
            .putLong(KEY_CURRENT_USER_ID, userId)
            .apply()
    }

    fun getCurrentUserId(): Long? {
        val userId = prefs.getLong(KEY_CURRENT_USER_ID, NO_USER_ID)
        return if (userId == NO_USER_ID) null else userId
    }

    fun hasActiveSession(): Boolean {
        return getCurrentUserId() != null
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_CURRENT_USER_ID)
            .remove(KEY_SESSION_START_TIME)
            .apply()
    }

    fun startSession() {
        prefs.edit()
            .putLong(KEY_SESSION_START_TIME, System.currentTimeMillis())
            .apply()
    }

    fun getSessionStartTime(): Long {
        return prefs.getLong(KEY_SESSION_START_TIME, System.currentTimeMillis())
    }

    companion object {
        private const val PREF_NAME = "micelios_session"
        private const val KEY_CURRENT_USER_ID = "current_user_id"
        private const val KEY_SESSION_START_TIME = "session_start_time"
        private const val NO_USER_ID = -1L
    }
}
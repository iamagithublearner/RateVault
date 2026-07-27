package com.example.ratevault.data

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL as libraryExecSQL

actual suspend fun SQLiteConnection.execSQL(sql: String) {
    this.libraryExecSQL(sql)
}

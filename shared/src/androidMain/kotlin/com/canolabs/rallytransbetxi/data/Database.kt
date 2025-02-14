package com.canolabs.rallytransbetxi.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.canolabs.rallytransbetxi.shared.data.sources.local.database.AppDatabase
import com.canolabs.rallytransbetxi.shared.utils.Constants

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val databaseFile = appContext.getDatabasePath(Constants.DATABASE_NAME)
    return Room.databaseBuilder(
        context = appContext,
        name = databaseFile.absolutePath
    )
}
package es.salvaaoliiver.secondevproject.main.bottombar.api.room

import android.content.Context
import androidx.room.Room

class DataStorageManager private constructor(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "nasa-pictures-database"
    ).build()

    fun nasaPictureDao(): NasaPictureDao {
        return database.nasaPictureDao()
    }

    companion object {
        @Volatile private var INSTANCE: DataStorageManager? = null

        fun getInstance(context: Context): DataStorageManager {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStorageManager(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
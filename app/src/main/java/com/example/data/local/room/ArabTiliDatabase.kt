package com.example.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.room.dao.LessonDao
import com.example.data.local.room.dao.LessonProgressDao
import com.example.data.local.room.dao.LevelDao
import com.example.data.local.room.dao.UserProgressDao
import com.example.data.local.room.entities.LessonEntity
import com.example.data.local.room.entities.LessonProgressEntity
import com.example.data.local.room.entities.LevelEntity
import com.example.data.local.room.entities.UserProgressEntity

@Database(
    entities = [
        LevelEntity::class,
        LessonEntity::class,
        UserProgressEntity::class,
        LessonProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ArabTiliDatabase : RoomDatabase() {

    abstract fun levelDao(): LevelDao
    abstract fun lessonDao(): LessonDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun lessonProgressDao(): LessonProgressDao

    companion object {
        @Volatile
        private var INSTANCE: ArabTiliDatabase? = null

        fun getInstance(context: Context): ArabTiliDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArabTiliDatabase::class.java,
                    "arab_tili_local.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

package com.example.micelios.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.micelios.data.local.dao.HyphaDao
import com.example.micelios.data.local.dao.MessageDao
import com.example.micelios.data.local.dao.MomentDao
import com.example.micelios.data.local.dao.ReactionDao
import com.example.micelios.data.local.dao.UserDao
import com.example.micelios.data.local.entity.HyphaEntity
import com.example.micelios.data.local.entity.HyphaMemberEntity
import com.example.micelios.data.local.entity.MessageEntity
import com.example.micelios.data.local.entity.MomentEntity
import com.example.micelios.data.local.entity.ReactionEntity
import com.example.micelios.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        HyphaEntity::class,
        MomentEntity::class,
        MessageEntity::class,
        ReactionEntity::class,
        HyphaMemberEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class MiceliosDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun hyphaDao(): HyphaDao
    abstract fun momentDao(): MomentDao
    abstract fun messageDao(): MessageDao

    abstract fun reactionDao(): ReactionDao

    companion object {
        @Volatile
        private var INSTANCE: MiceliosDatabase? = null

        fun getDatabase(context: Context): MiceliosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MiceliosDatabase::class.java,
                    "micelios_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
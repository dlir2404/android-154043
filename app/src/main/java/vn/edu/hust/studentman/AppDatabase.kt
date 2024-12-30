package vn.edu.hust.studentman

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StudentModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}
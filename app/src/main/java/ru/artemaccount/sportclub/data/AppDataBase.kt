package ru.artemaccount.sportclub.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun personDao(): PersonDAO
}
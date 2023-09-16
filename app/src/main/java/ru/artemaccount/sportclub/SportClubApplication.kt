package ru.artemaccount.sportclub

import android.app.Application
import androidx.room.Room
import ru.artemaccount.sportclub.data.AppDataBase
import ru.artemaccount.sportclub.data.PersonDAO

class SportClubApplication : Application() {

    companion object {
        lateinit var db: PersonDAO
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "persons"
        ).build().personDao()
    }
}
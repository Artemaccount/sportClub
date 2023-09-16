package ru.artemaccount.sportclub.data

import androidx.room.*

@Dao
interface PersonDAO {

    @Query("SELECT * FROM person")
    suspend fun getAll(): List<Person>

    @Insert
    suspend fun insert(persons: Person)

    @Update
    suspend fun update(person: Person)

    @Query("DELETE FROM person WHERE id = :personId")
    suspend fun deletePersonById(personId: Int)
}
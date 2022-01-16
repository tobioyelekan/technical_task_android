package com.example.userapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.userapp.data.source.local.UserDatabase
import com.example.userapp.data.source.local.dao.UserDao
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class DbSetup {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: UserDatabase
    protected lateinit var userDao: UserDao

    @Before
    fun init() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).build()

        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

}
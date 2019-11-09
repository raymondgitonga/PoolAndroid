package com.tosh.poolandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tosh.poolandroid.Remote.RetrofitApi
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class UserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var retrofitApi: RetrofitApi

    lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
       
    }

    @After
    fun tearDown() {
    }

    @Test
    fun userLogin() {
       
    }

    @Test
    fun addUserPhone() {
    }

    @Test
    fun userRegister() {
    }
}
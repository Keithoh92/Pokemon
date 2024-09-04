package com.example.pokemon

import androidx.annotation.CallSuper
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
abstract class BaseTest {

    @BeforeEach
    fun before() {
        setUp()
    }

    @CallSuper
    open fun setUp() {}

    @AfterEach
    open fun after() {
        tearDown()
    }

    @CallSuper
    open fun tearDown() {}
}
package com.example.cheatsignal.data.source.local.preferences

import com.example.cheatsignal.data.source.local.datastore.SettingsDataStore
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PreferencesManagerTest {
    @MockK
    private lateinit var settingsDataStore: SettingsDataStore
    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        preferencesManager = PreferencesManager(settingsDataStore)
    }

    // TODO: Implement PreferencesManager tests
}

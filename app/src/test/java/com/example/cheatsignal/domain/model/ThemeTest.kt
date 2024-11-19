package com.example.cheatsignal.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ThemeTest {
    
    @Test
    fun `fromString with valid LIGHT theme returns LIGHT`() {
        // When
        val result = Theme.fromString("LIGHT")
        
        // Then
        assertThat(result).isEqualTo(Theme.LIGHT)
    }
    
    @Test
    fun `fromString with valid DARK theme returns DARK`() {
        // When
        val result = Theme.fromString("DARK")
        
        // Then
        assertThat(result).isEqualTo(Theme.DARK)
    }
    
    @Test
    fun `fromString with valid SYSTEM theme returns SYSTEM`() {
        // When
        val result = Theme.fromString("SYSTEM")
        
        // Then
        assertThat(result).isEqualTo(Theme.SYSTEM)
    }
    
    @Test
    fun `fromString is case insensitive`() {
        // When
        val lightResult = Theme.fromString("light")
        val darkResult = Theme.fromString("dark")
        val systemResult = Theme.fromString("system")
        
        // Then
        assertThat(lightResult).isEqualTo(Theme.LIGHT)
        assertThat(darkResult).isEqualTo(Theme.DARK)
        assertThat(systemResult).isEqualTo(Theme.SYSTEM)
    }
    
    @Test
    fun `fromString with invalid theme returns SYSTEM`() {
        // When
        val result = Theme.fromString("INVALID_THEME")
        
        // Then
        assertThat(result).isEqualTo(Theme.SYSTEM)
    }
    
    @Test
    fun `fromString with empty string returns SYSTEM`() {
        // When
        val result = Theme.fromString("")
        
        // Then
        assertThat(result).isEqualTo(Theme.SYSTEM)
    }
}

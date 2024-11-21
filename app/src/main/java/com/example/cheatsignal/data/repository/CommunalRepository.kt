package com.example.cheatsignal.data.repository

import com.example.cheatsignal.data.local.CommunalDao
import com.example.cheatsignal.data.model.CommunalAddress
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunalRepository @Inject constructor(
    private val communalDao: CommunalDao
) {
    fun getAddresses(): Flow<List<CommunalAddress>> = communalDao.getAddresses()

    suspend fun saveAddress(address: CommunalAddress) {
        communalDao.insertAddress(address)
    }

    suspend fun clearAllAddresses() = communalDao.clearAll()
}

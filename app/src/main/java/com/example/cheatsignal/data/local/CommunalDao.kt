package com.example.cheatsignal.data.local

import androidx.room.*
import com.example.cheatsignal.data.model.CommunalAddress
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunalDao {
    @Query("SELECT * FROM communal_addresses")
    fun getAddresses(): Flow<List<CommunalAddress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: CommunalAddress)

    @Delete
    suspend fun deleteAddress(address: CommunalAddress)

    @Query("DELETE FROM communal_addresses")
    suspend fun clearAll()
}

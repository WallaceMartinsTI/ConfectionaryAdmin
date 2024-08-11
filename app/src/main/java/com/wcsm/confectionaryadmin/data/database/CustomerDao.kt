package com.wcsm.confectionaryadmin.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wcsm.confectionaryadmin.data.model.Customer
import com.wcsm.confectionaryadmin.data.model.CustomerWithOrders

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers")
    suspend fun getAllCustomers(): List<Customer>

    @Transaction
    @Query("SELECT * FROM customers")
    suspend fun getCustomersWithOrders(): List<CustomerWithOrders>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

}
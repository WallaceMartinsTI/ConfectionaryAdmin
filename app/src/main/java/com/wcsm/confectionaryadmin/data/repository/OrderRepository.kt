package com.wcsm.confectionaryadmin.data.repository

import com.wcsm.confectionaryadmin.data.model.Order
import com.wcsm.confectionaryadmin.data.model.OrderWithCustomer

interface OrderRepository {

    suspend fun getAllOrdersWithCustomer(): List<OrderWithCustomer>

    suspend fun insertOrder(order: Order)

    suspend fun deleteOrder(order: Order)

    suspend fun getOrdersByOrderDateFilteredByMonthAndYear(
        startOfMonth: Long,
        endOfMonth: Long
    ): List<Order>

    suspend fun getOrdersByDeliverDateFilteredByMonthAndYear(
        startOfMonth: Long,
        endOfMonth: Long
    ): List<Order>

}
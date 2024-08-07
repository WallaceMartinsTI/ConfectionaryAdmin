package com.wcsm.confectionaryadmin.data.repository

import android.util.Log
import com.wcsm.confectionaryadmin.data.database.OrderDao
import com.wcsm.confectionaryadmin.data.model.Order
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {
    override suspend fun getAllOrders(): List<Order> {
        return orderDao.getAllOrders()
    }

    override suspend fun insertOrder(order: Order) {
        Log.i("#-# TESTE #-#", "REPOSITORY IMPL order recebida: $order")
        orderDao.insertOrder(order)
    }

    override suspend fun getOrdersByOrderDateFilteredByMonthAndYear(
        startOfMonth: Long,
        endOfMonth: Long
    ): List<Order> {
        return orderDao.getOrdersByOrderDateFilteredByMonthAndYear(startOfMonth, endOfMonth)
    }

    override suspend fun getOrdersByDeliverDateFilteredByMonthAndYear(
        startOfMonth: Long,
        endOfMonth: Long
    ): List<Order> {
        return orderDao.getOrdersByDeliverDateFilteredByMonthAndYear(startOfMonth, endOfMonth)
    }

}
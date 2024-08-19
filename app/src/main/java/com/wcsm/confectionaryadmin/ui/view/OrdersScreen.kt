package com.wcsm.confectionaryadmin.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wcsm.confectionaryadmin.R
import com.wcsm.confectionaryadmin.data.model.entities.Order
import com.wcsm.confectionaryadmin.data.model.types.OrderStatus
import com.wcsm.confectionaryadmin.data.model.entities.OrderWithCustomer
import com.wcsm.confectionaryadmin.data.model.navigation.Screen
import com.wcsm.confectionaryadmin.data.model.types.FilterType
import com.wcsm.confectionaryadmin.data.model.types.OrderDateType
import com.wcsm.confectionaryadmin.ui.components.ChangeStatusDialog
import com.wcsm.confectionaryadmin.ui.components.DeletionConfirmDialog
import com.wcsm.confectionaryadmin.ui.components.OrderCard
import com.wcsm.confectionaryadmin.ui.components.OrdersFilterContainer
import com.wcsm.confectionaryadmin.ui.components.OrdersFilterDialog
import com.wcsm.confectionaryadmin.ui.theme.AppBackground
import com.wcsm.confectionaryadmin.ui.theme.AppTitleGradient
import com.wcsm.confectionaryadmin.ui.theme.ConfectionaryAdminTheme
import com.wcsm.confectionaryadmin.ui.theme.InterFontFamily
import com.wcsm.confectionaryadmin.ui.theme.Primary
import com.wcsm.confectionaryadmin.ui.util.convertDateMonthYearStringToLong
import com.wcsm.confectionaryadmin.ui.util.convertStringToDateMillis
import com.wcsm.confectionaryadmin.ui.util.getNextStatus
import com.wcsm.confectionaryadmin.ui.util.getYearAndMonthFromTimeInMillis
import com.wcsm.confectionaryadmin.ui.util.toStatusString
import com.wcsm.confectionaryadmin.ui.viewmodel.OrdersViewModel

val ordersMock = listOf(
    Order(
        orderId = 0,
        customerOwnerId = 0,
        title = "Bolo decenouro para o cliente com cobertura pr",
        description = "Recheio de chocolate e mousse de morango",
        price = 120.50,
        status = OrderStatus.QUOTATION,
        orderDate = convertStringToDateMillis("15/02/2024 16:30") ,
        deliverDate = convertStringToDateMillis("20/02/2024 17:25")
    ),
    Order(
        orderId = 1,
        customerOwnerId = 1,
        title = "Doce Brigadeiro 100u",
        description = "100 unidades de Brigadeiros skajdhiashdisahudiuhasiudhas suadhiashd idsahduias uiash iusah iduashi sadasdasd",
        price = 115.00,
        status = OrderStatus.CONFIRMED,
        orderDate = convertStringToDateMillis("25/02/2024 12:30"),
        deliverDate = convertStringToDateMillis("28/02/2024 16:22")
    ),
    Order(
        orderId = 2,
        customerOwnerId = 2,
        title = "Bolo de Aniversário",
        description = "Massa de Chocolate e recheio de prestígio",
        price = 95.25,
        status = OrderStatus.IN_PRODUCTION,
        orderDate = convertStringToDateMillis("28/03/2024 09:30"),
        deliverDate = convertStringToDateMillis("28/03/2024 09:30")
    ),
    Order(
        orderId = 3,
        customerOwnerId = 3,
        title = "Bolo de Aniversário com nome meio grande vamos ver",
        description = "Massa de Chocolate e recheio de prestígio",
        price = 95.25,
        status = OrderStatus.DELIVERED,
        orderDate = convertStringToDateMillis("28/03/2024 11:00"),
        deliverDate = convertStringToDateMillis("10/04/2024 16:00")
    )
)

@Composable
fun OrdersScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    ordersViewModel: OrdersViewModel
) {
    val ordersWithCustomer by ordersViewModel.ordersWithCustomer.collectAsState()
    val orderToChangeStatus by ordersViewModel.orderToChangeStatus.collectAsState()
    val filterType by ordersViewModel.filterType.collectAsState()
    val orderDateType by ordersViewModel.orderDateType.collectAsState()

    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    var invertedList by rememberSaveable { mutableStateOf(false) }

    var showFilterDialog by remember { mutableStateOf(false) }
    var showChangeOrderStatusDialog by remember { mutableStateOf(false) }
    var showDeleteOrderDialog by remember { mutableStateOf(false) }
    var orderWithCustomerToBeDeleted by remember { mutableStateOf<OrderWithCustomer?>(null) }

    val customBlur = if(showFilterDialog) 8.dp else 0.dp

    val filterResult by ordersViewModel.filterResult.collectAsState()
    var filterTextToShow by remember { mutableStateOf("") }

    var ordersList by remember { mutableStateOf(ordersWithCustomer) }

    LaunchedEffect(ordersWithCustomer) {
        ordersList = ordersWithCustomer
    }

    LaunchedEffect(filterResult, invertedList) {
        when(filterType) {
            FilterType.DATE -> {
                filterTextToShow = "Data: $filterResult"

                val result = convertDateMonthYearStringToLong(filterResult)
                val (filterYear, filterMonth) = getYearAndMonthFromTimeInMillis(result)

                ordersList = ordersWithCustomer.filter {
                    val (orderYear, orderMonth) = if(orderDateType == OrderDateType.ORDER_DATE) {
                        getYearAndMonthFromTimeInMillis(it.order.orderDate)
                    } else {
                        getYearAndMonthFromTimeInMillis(it.order.deliverDate)
                    }

                    orderYear == filterYear && orderMonth == filterMonth
                }
                ordersList = if(invertedList) ordersList.reversed() else ordersList
            }

            FilterType.STATUS -> {
                filterTextToShow = "Status: $filterResult"
                ordersList = ordersWithCustomer.filter {
                    it.order.status.toStatusString() == filterResult
                }
                ordersList = if(invertedList) ordersList.reversed() else ordersList
            }

            else -> {
                ordersList = if(invertedList) ordersWithCustomer.reversed() else ordersWithCustomer
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            ordersViewModel.updateFilterResult(newResult = "")
            ordersViewModel.updateFilterType(filterType = null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .background(AppBackground)
                .fillMaxSize()
                .blur(customBlur),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.screen_title_orders_screen),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                style = TextStyle(
                    brush = AppTitleGradient
                ),
                modifier = Modifier.padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChangeCircle,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            invertedList = !invertedList
                        }
                )

                OrdersFilterContainer(
                    text = filterTextToShow
                ) {
                    showFilterDialog = true
                }

                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            filterTextToShow = ""
                            ordersViewModel.updateFilterResult(newResult = "")
                            ordersViewModel.updateFilterType(filterType = null)
                        }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier.width(300.dp),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                contentPadding = paddingValues
            ) {
                items(ordersList) {
                    OrderCard(
                        order = it.order,
                        isExpanded = expandedStates[it.order.orderId] ?: false,
                        customerOwnerName = it.customer.name,
                        onExpandChange = { expanded ->
                            expandedStates[it.order.orderId] = expanded
                        },
                        onEdit = { _ ->
                            ordersViewModel.updateOrderToBeEditted(it)
                            navController.navigate(Screen.CreateOrder.route)
                        },
                        onDelete = { _ ->
                            orderWithCustomerToBeDeleted = it
                            showDeleteOrderDialog = true
                        },
                        onChangeStatus = {
                            ordersViewModel.updateOrderToChangeStatus(it.order)
                            showChangeOrderStatusDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        if(showDeleteOrderDialog && orderWithCustomerToBeDeleted != null) {
            val order = orderWithCustomerToBeDeleted!!.order
            val customer = orderWithCustomerToBeDeleted!!.customer
            DeletionConfirmDialog(
                order = order,
                customerOwnerName = customer.name,
                customer = null,
                onConfirm = {
                    ordersViewModel.deleteOrder(order)
                    showDeleteOrderDialog = false
                }
            ) {
                showDeleteOrderDialog = false
            }
        }

        if(showFilterDialog) {
            Dialog(onDismissRequest = { showFilterDialog = false }) {
                OrdersFilterDialog(
                    ordersViewModel = ordersViewModel,
                ) {
                    showFilterDialog = false
                }
            }
        }

        if(showChangeOrderStatusDialog && orderToChangeStatus != null) {
            Dialog(onDismissRequest = {
                showChangeOrderStatusDialog = false
                ordersViewModel.updateOrderToChangeStatus(null)
            }) {
                ChangeStatusDialog(
                    order = orderToChangeStatus!!,
                    onDissmiss = {
                        showChangeOrderStatusDialog = false
                        ordersViewModel.updateOrderToChangeStatus(null)
                    }
                ) {
                    val statusChangedOrder = orderToChangeStatus!!.copy(
                        status = getNextStatus(orderToChangeStatus!!.status)
                    )
                    ordersViewModel.updateOrderStatus(statusChangedOrder)
                    ordersViewModel.updateOrderToChangeStatus(null)
                    showChangeOrderStatusDialog = false
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrdersScreenPreview(
    ordersViewModel: OrdersViewModel = hiltViewModel()
) {
    ConfectionaryAdminTheme {
        val navController = rememberNavController()
        val paddingValues = PaddingValues()
        OrdersScreen(navController, paddingValues, ordersViewModel)
    }
}
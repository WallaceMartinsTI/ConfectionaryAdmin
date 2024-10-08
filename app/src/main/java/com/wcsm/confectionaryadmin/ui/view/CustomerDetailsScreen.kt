package com.wcsm.confectionaryadmin.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wcsm.confectionaryadmin.R
import com.wcsm.confectionaryadmin.data.model.entities.Customer
import com.wcsm.confectionaryadmin.data.model.entities.Order
import com.wcsm.confectionaryadmin.data.model.types.OrderStatus
import com.wcsm.confectionaryadmin.ui.components.CustomLoading
import com.wcsm.confectionaryadmin.ui.components.CustomTextField
import com.wcsm.confectionaryadmin.ui.components.CustomerOrdersContainer
import com.wcsm.confectionaryadmin.ui.components.DeleteButton
import com.wcsm.confectionaryadmin.ui.components.DialogDeletionConfirm
import com.wcsm.confectionaryadmin.ui.components.PrimaryButton
import com.wcsm.confectionaryadmin.ui.theme.AppBackgroundColor
import com.wcsm.confectionaryadmin.ui.theme.ConfectionaryAdminTheme
import com.wcsm.confectionaryadmin.ui.theme.InterFontFamily
import com.wcsm.confectionaryadmin.ui.theme.InvertedAppBackgroundColor
import com.wcsm.confectionaryadmin.ui.theme.PrimaryColor
import com.wcsm.confectionaryadmin.ui.theme.StrongDarkPurpleColor
import com.wcsm.confectionaryadmin.ui.util.PhoneNumberVisualTransformation
import com.wcsm.confectionaryadmin.ui.util.convertMillisToString
import com.wcsm.confectionaryadmin.ui.util.showToastMessage
import com.wcsm.confectionaryadmin.ui.util.toBrazillianDateFormat
import com.wcsm.confectionaryadmin.ui.viewmodel.CreateCustomerViewModel
import com.wcsm.confectionaryadmin.ui.viewmodel.CustomersViewModel
import com.wcsm.confectionaryadmin.ui.viewmodel.OrdersViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreen(
    navController: NavController,
    ordersViewModel: OrdersViewModel,
    customersViewModel: CustomersViewModel,
    createCustomerViewModel: CreateCustomerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val selectedCustomer by customersViewModel.selectedCustomer.collectAsState()
    val isCustomerDeleted by customersViewModel.isCustomerDeleted.collectAsState()

    val customerOrders by ordersViewModel.customerOrders.collectAsState()

    val customerState by createCustomerViewModel.customerState.collectAsState()
    val customerUpdated by createCustomerViewModel.customerUpdated.collectAsState()
    val customerSyncState by customersViewModel.customerSyncState.collectAsState()

    var customer: Customer? by rememberSaveable { mutableStateOf(null) }
    var orders: List<Order>? by rememberSaveable { mutableStateOf(null) }

    var showDeleteCustomerDialog by remember { mutableStateOf(false) }

    var isCustomerDetailsScreenLoading by rememberSaveable { mutableStateOf(true) }

    var customerId by rememberSaveable { mutableStateOf<String?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var dateOfBirth by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    var ordersQuantity by rememberSaveable { mutableIntStateOf(0) }
    var customerSince by rememberSaveable { mutableStateOf("") }

    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf("") }

    var genderDropdownExpanded by remember { mutableStateOf(false) }

    var isCustomerOrdersOpen by remember { mutableStateOf(false) }

    val customerIcon = when(customer?.gender) {
        "Masculino" -> painterResource(id = R.drawable.male)
        "Feminino" -> painterResource(id = R.drawable.female)
        else -> painterResource(id = R.drawable.others)
    }

    LaunchedEffect(selectedCustomer) {
        Log.i("#-# TESTE #-#", "CustomerDetailScreen ENTROU LaunchedEffect(selectedCustomer)")
        Log.i("#-# TESTE #-#", "selectedCustomer: $selectedCustomer")
        if(selectedCustomer != null) {
            Log.i("#-# TESTE #-#", "Entrou: selectedCustomer != null")
            Log.i("#-# TESTE #-#", "selectedCustomer: $selectedCustomer")
            ordersViewModel.getOrdersByCustomer(selectedCustomer!!.customerId)
        }
    }

    LaunchedEffect(customerUpdated) {
        if(customerUpdated) {
            createCustomerViewModel.updateCustomerUpdated(false)
            customersViewModel.getAllCustomers()
            customersViewModel.updateCustomerSyncState(
                customerSyncState.copy(
                    isSincronized = false
                )
            )
            showToastMessage(context, "Cliente atualizado.")
        }
    }

    LaunchedEffect(isCustomerDeleted) {
        if(isCustomerDeleted) {
            ordersViewModel.getAllOrders()
            customersViewModel.updateCustomerSyncState(
                customerSyncState.copy(
                    isSincronized = false
                )
            )
            customersViewModel.updateCustomerDeletes(false)
            navController.popBackStack()
        }
    }

    LaunchedEffect(customerOrders) {
        if(customerOrders != null) {
            customer = selectedCustomer
            orders = customerOrders

            customerId = customer!!.customerId
            name = customer!!.name
            email = customer!!.email ?: ""
            phone = customer!!.phone ?: ""
            gender = customer!!.gender ?: ""
            dateOfBirth = customer!!.dateOfBirth ?: ""
            address = customer!!.address ?: ""
            notes = customer!!.notes ?: ""
            ordersQuantity = orders!!.filter { it.status == OrderStatus.DELIVERED }.size
            customerSince = convertMillisToString(customer!!.customerSince)

            delay(1000)
            isCustomerDetailsScreenLoading = false
        }
    }

    LaunchedEffect(selectedDate) {
        if(selectedDate.isNotEmpty()) {
            dateOfBirth = selectedDate
        }
    }

    if(isCustomerDetailsScreenLoading) {
        Column(
            modifier = Modifier
                .background(AppBackgroundColor)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomLoading(size = 80.dp)
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(isCustomerDetailsScreenLoading) {
                Text(text = "CARREGANDO...")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = InvertedAppBackgroundColor),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Icon(
                            painter = customerIcon,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )

                        Text(
                            text = name,
                            color = PrimaryColor,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                        )

                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    navController.popBackStack()
                                    customersViewModel.updateSelectedCustomer(null)
                                }
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        color = Color.White
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        CustomTextField(
                            label = stringResource(id = R.string.textfield_label_name),
                            placeholder = stringResource(id = R.string.textfield_placeholder_name),
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            errorMessage = customerState.nameErrorMessage,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            value = name
                        ) {
                            name = it
                        }

                        CustomTextField(
                            label = stringResource(id = R.string.textfield_label_email),
                            placeholder = stringResource(id = R.string.textfield_placeholder_email),
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                            errorMessage = null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            value = email
                        ) {
                            email = it
                        }

                        CustomTextField(
                            label = stringResource(id = R.string.textfield_label_phone),
                            placeholder = stringResource(id = R.string.textfield_placeholder_phone),
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next,
                            visualTransformation = PhoneNumberVisualTransformation(),
                            errorMessage = null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            value = phone
                        ) {
                            phone = it
                        }

                        Box {
                            ExposedDropdownMenuBox(
                                expanded = genderDropdownExpanded,
                                onExpandedChange = {
                                    genderDropdownExpanded = !genderDropdownExpanded
                                }
                            ) {
                                CustomTextField(
                                    modifier = Modifier.menuAnchor(),
                                    label = stringResource(id = R.string.textfield_label_gender),
                                    placeholder = "",
                                    errorMessage = null,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            imageVector =
                                            if(genderDropdownExpanded) Icons.Filled.KeyboardArrowUp
                                            else Icons.Filled.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = PrimaryColor
                                        )
                                    },
                                    singleLine = true,
                                    readOnly = true,
                                    value = gender
                                ) {
                                    genderDropdownExpanded = !genderDropdownExpanded
                                }

                                ExposedDropdownMenu(
                                    expanded = genderDropdownExpanded,
                                    onDismissRequest = { genderDropdownExpanded = false },
                                    modifier = Modifier.background(color = StrongDarkPurpleColor)
                                ) {
                                    val genderOptions = listOf(
                                        stringResource(id = R.string.gender_male),
                                        stringResource(id = R.string.gender_female),
                                        stringResource(id = R.string.gender_other)
                                    )

                                    genderOptions.forEach {
                                        val icon = when(it) {
                                            "Masculino" -> Icons.Default.Male
                                            "Feminino" -> Icons.Default.Female
                                            else -> Icons.Default.Transgender
                                        }
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = it,
                                                    color = Color.White,
                                                    fontFamily = InterFontFamily,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = null,
                                                    tint = Color.White
                                                )
                                            },
                                            onClick = {
                                                gender = it
                                                genderDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        if(showDatePickerDialog) {
                            DatePickerDialog(
                                onDismissRequest = {
                                    showDatePickerDialog = false
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            datePickerState.selectedDateMillis?.let { millis ->
                                                selectedDate = millis.toBrazillianDateFormat()
                                            }
                                            showDatePickerDialog = false
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = PrimaryColor
                                        )
                                    ) {
                                        Text(text = stringResource(id = R.string.choose_date))
                                    }
                                }
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    showModeToggle = false,
                                    colors = DatePickerDefaults.colors(
                                        headlineContentColor = PrimaryColor,
                                        weekdayContentColor = PrimaryColor,
                                        currentYearContentColor = PrimaryColor,
                                        selectedYearContainerColor = PrimaryColor,
                                        selectedDayContainerColor = PrimaryColor,
                                        todayContentColor = PrimaryColor,
                                        todayDateBorderColor = PrimaryColor
                                    )
                                )
                            }
                        }

                        CustomTextField(
                            label = stringResource(id = R.string.textfield_label_date_of_birth),
                            placeholder = "",
                            modifier = Modifier
                                .onFocusEvent {
                                    if(it.isFocused) {
                                        showDatePickerDialog = true
                                    }
                                },
                            errorMessage = null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            value = dateOfBirth
                        ) { }

                        CustomTextField(
                            label = stringResource(id = R.string.textfield_label_address),
                            placeholder = stringResource(id = R.string.textfield_placeholder_address),
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            singleLine = false,
                            errorMessage = null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AddLocation,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            value = address
                        ) {
                            address = it
                        }

                        CustomTextField(
                            label = stringResource(id = R.string.textfield_label_notes),
                            placeholder = stringResource(id = R.string.textfield_placeholder_notes),
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            singleLine = false,
                            errorMessage = null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.Note,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            value = notes
                        ) {
                            notes = it
                        }

                        CustomTextField(
                            label = stringResource(id = R.string.textfield_label_orders),
                            placeholder = "",
                            readOnly = true,
                            errorMessage = null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            value = ordersQuantity.toString()
                        ) {
                            if(it.all { it.isDigit() }) {
                                if(ordersQuantity >= 0) {
                                    ordersQuantity = it.toInt()
                                }
                            }
                        }

                        CustomTextField(
                            label = stringResource(id = R.string.textfield_label_customer_since),
                            placeholder = "",
                            readOnly = true,
                            errorMessage = null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            value = customerSince
                        ) { }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    PrimaryButton(text = stringResource(id = R.string.btn_text_save_edit)) {
                        createCustomerViewModel.updateCreateCustomerState(
                            customerState.copy(
                                customerId = customerId,
                                name = name,
                                email = email,
                                phone = phone,
                                gender = gender,
                                dateOfBirth = dateOfBirth,
                                address = address,
                                notes = notes,
                                ordersQuantity = ordersQuantity,
                                customerSince = customerSince
                            )
                        )

                        createCustomerViewModel.saveCustomer(isUpdateCustomer = true)
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                        color = Color.White
                    )

                    PrimaryButton(text = stringResource(id = R.string.btn_text_show_orders)) {
                        isCustomerOrdersOpen = true
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DeleteButton(text = stringResource(id = R.string.btn_text_delete_customer)) {
                        showDeleteCustomerDialog = true
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                if(showDeleteCustomerDialog && customer != null) {
                    DialogDeletionConfirm(
                        order = null,
                        customer = customer,
                        onConfirm = {
                            customersViewModel.deleteCustomer(customer!!)
                            showDeleteCustomerDialog = false
                        }
                    ) {
                        showDeleteCustomerDialog = false
                    }
                }

                if(isCustomerOrdersOpen) {
                    Dialog(onDismissRequest = { isCustomerOrdersOpen = false }) {
                        CustomerOrdersContainer(
                            customer = customer!!,
                            orders = orders!!,
                            isCustomerDetailsScreen = true
                        ) {
                            isCustomerOrdersOpen = false
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomerDetailsScreenPreview(
    ordersViewModel: OrdersViewModel = hiltViewModel(),
    customersViewModel: CustomersViewModel = hiltViewModel()
) {
    ConfectionaryAdminTheme {
        val navController = rememberNavController()
        CustomerDetailsScreen(
            navController = navController,
            ordersViewModel = ordersViewModel,
            customersViewModel = customersViewModel
        )
    }
}
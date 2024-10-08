package com.wcsm.confectionaryadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.confectionaryadmin.R
import com.wcsm.confectionaryadmin.data.model.entities.Order
import com.wcsm.confectionaryadmin.data.model.types.OrderStatus
import com.wcsm.confectionaryadmin.ui.theme.BrownColor
import com.wcsm.confectionaryadmin.ui.theme.ConfectionaryAdminTheme
import com.wcsm.confectionaryadmin.ui.theme.InProductionStatusColor
import com.wcsm.confectionaryadmin.ui.theme.InterFontFamily
import com.wcsm.confectionaryadmin.ui.theme.InvertedAppBackgroundColor
import com.wcsm.confectionaryadmin.ui.theme.LightRedColor
import com.wcsm.confectionaryadmin.ui.theme.PrimaryColor
import com.wcsm.confectionaryadmin.ui.theme.ValueColor
import com.wcsm.confectionaryadmin.ui.util.convertMillisToString
import com.wcsm.confectionaryadmin.ui.util.customersMock
import com.wcsm.confectionaryadmin.ui.util.getStatusColor
import com.wcsm.confectionaryadmin.ui.util.ordersMock
import com.wcsm.confectionaryadmin.ui.util.toBRL
import com.wcsm.confectionaryadmin.ui.util.toStatusString

@Composable
fun OrderCard(
    order: Order,
    isExpanded: Boolean,
    isCustomerDetailsScreen: Boolean = false,
    customerOwnerName: String? = null,
    onEdit: (order: Order) -> Unit,
    onDelete: (order: Order) -> Unit,
    onChangeStatus: () -> Unit,
    onExpandChange: (Boolean) -> Unit
) {
    val blockedOrderStatus = listOf(OrderStatus.DELIVERED, OrderStatus.CANCELLED)

    if(isExpanded) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(brush = InvertedAppBackgroundColor)
                .border(1.dp, PrimaryColor, RoundedCornerShape(15.dp))
                .width(328.dp)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.title,
                    color = PrimaryColor,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onExpandChange(false) }
                )
            }

            HorizontalDivider(color = Color.White)

            Text(
                text = order.description,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            HorizontalDivider(color = Color.White)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.price.toBRL(),
                    color = ValueColor,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Status: ${order.status.toStatusString()}",
                    color = order.status.getStatusColor(),
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(color = Color.White)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Pedido: ${convertMillisToString(order.orderDate)}",
                        color = BrownColor,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Entrega: ${convertMillisToString(order.deliverDate)}",
                        color = PrimaryColor,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if(customerOwnerName?.isNotEmpty() == true) {
                HorizontalDivider(color = Color.White)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cliente: ",
                        color = PrimaryColor,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = customerOwnerName,
                        color = Color.White,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if(!isCustomerDetailsScreen) {
                HorizontalDivider(color = Color.White)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomActionButton(
                        text = stringResource(id = R.string.btn_text_edit),
                        color = InProductionStatusColor,
                        icon = Icons.Default.Edit
                    ) {
                        onEdit(order)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    CustomActionButton(
                        text = stringResource(id = R.string.btn_text_delete),
                        color = LightRedColor,
                        icon = Icons.Default.Delete
                    ) {
                        onDelete(order)
                    }
                }

                if(order.status !in blockedOrderStatus) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp), color = Color.White)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                                .background(PrimaryColor)
                                .border(1.dp, Color.White, RoundedCornerShape(15.dp))
                                .padding(vertical = 4.dp, horizontal = 12.dp)
                                .clickable {
                                    onChangeStatus()
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.next_status_text).uppercase(),
                                color = Color.White,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.next_custom_icon),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(brush = InvertedAppBackgroundColor)
                .border(1.dp, PrimaryColor, RoundedCornerShape(15.dp))
                .width(328.dp)
                .padding(12.dp)
                .clickable { onExpandChange(true) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = order.title,
                    color = PrimaryColor,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        tint = order.status.getStatusColor(),
                        modifier = Modifier.size(12.dp)
                    )

                    Text(
                        text = order.status.toStatusString(),
                        color = order.status.getStatusColor(),
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Text(
                    text = "Pedido: ${convertMillisToString(order.orderDate)}",
                    color = Color.White,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Entrega: ${convertMillisToString(order.deliverDate)}",
                    color = Color.White,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                )

                if(customerOwnerName?.isNotEmpty() == true) {
                    HorizontalDivider(color = PrimaryColor, modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cliente: ",
                            color = PrimaryColor,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = customerOwnerName,
                            color = Color.White,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun CustomActionButton(
    text: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(PrimaryColor)
            .border(1.dp, PrimaryColor, RoundedCornerShape(15.dp))
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = color,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderCardPreview() {
    ConfectionaryAdminTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val customerOwner = customersMock[0]

            OrderCard(
                order = ordersMock[0],
                isExpanded = false,
                customerOwnerName = customerOwner.name,
                onExpandChange = {},
                onEdit = {},
                onDelete = {},
                onChangeStatus = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            OrderCard(
                order = ordersMock[0],
                isExpanded = true,
                customerOwnerName = customerOwner.name,
                onExpandChange = {},
                onEdit = {},
                onDelete = {},
                onChangeStatus = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            ordersMock.forEach {
                OrderCard(
                    order = it,
                    isExpanded = false,
                    customerOwnerName = customerOwner.name,
                    onExpandChange = {},
                    onEdit = {},
                    onDelete = {},
                    onChangeStatus = {}
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun CustomActionButtonPreview() {
    ConfectionaryAdminTheme {
        CustomActionButton(
            text = "EDITAR",
            color = InProductionStatusColor,
            icon = Icons.Default.Edit,
        ) {}
    }
}
package com.wcsm.confectionaryadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.confectionaryadmin.ui.theme.ConfectionaryAdminTheme
import com.wcsm.confectionaryadmin.ui.theme.InterFontFamily
import com.wcsm.confectionaryadmin.ui.theme.LightRedColor

@Composable
fun CustomErrorMessage(
    width: Dp = 280.dp,
    errorMessage: String,
    errorIcon: @Composable (() -> Unit) = {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Warning icon",
            tint = Color.White
        )
    }
) {
    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .width(width)
            .clip(RoundedCornerShape(10.dp))
            .background(LightRedColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        errorIcon()

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = errorMessage,
            color = Color.White,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomErrorMessagePreview() {
    ConfectionaryAdminTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomErrorMessage(errorMessage = "Não foi possível realizar a ação.")
            Spacer(modifier = Modifier.height(32.dp))
            CustomErrorMessage(errorMessage = "Informe uma descrição pois o valor informado nao pode ser vazio.")
        }
    }
}
package com.wcsm.confectionaryadmin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.confectionaryadmin.ui.theme.AppBackgroundColor
import com.wcsm.confectionaryadmin.ui.theme.AppTitleGradientColor
import com.wcsm.confectionaryadmin.ui.theme.ConfectionaryAdminTheme
import com.wcsm.confectionaryadmin.ui.theme.InterFontFamily
import com.wcsm.confectionaryadmin.ui.theme.PrimaryColor

@Composable
fun CustomTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackgroundColor)
            .padding(top = 12.dp).then(modifier)
    ) {
        IconButton(
            onClick = { onBackPressed() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = PrimaryColor
            )
        }

        Text(
            text = title,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            style = TextStyle(
                brush = AppTitleGradientColor
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun CustomTopAppBarPreview() {
    ConfectionaryAdminTheme {
        CustomTopAppBar("REGISTRO") {}
    }
}
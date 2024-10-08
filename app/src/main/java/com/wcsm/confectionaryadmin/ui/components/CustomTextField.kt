package com.wcsm.confectionaryadmin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.confectionaryadmin.ui.theme.ConfectionaryAdminTheme
import com.wcsm.confectionaryadmin.ui.theme.InterFontFamily
import com.wcsm.confectionaryadmin.ui.theme.PrimaryColor
import com.wcsm.confectionaryadmin.ui.theme.TextFieldBackgroundColor

@Composable
fun CustomTextField(
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    width: Dp = 280.dp,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    charactereCounter: Boolean? = false,
    charactereLimit: Int? = null,
    errorMessage: String?,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    value: String,
    onValueChange: (newValue: String) -> Unit
) {
    val isError = errorMessage != null

    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = TextFieldBackgroundColor,
        unfocusedContainerColor = TextFieldBackgroundColor,
        errorContainerColor = TextFieldBackgroundColor,
        cursorColor = PrimaryColor,
        focusedBorderColor = PrimaryColor,
        unfocusedBorderColor = PrimaryColor,
        selectionColors = TextSelectionColors(
            PrimaryColor, Color.Transparent
        )
    )

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if(charactereCounter != null && charactereLimit != null) {
                if(value.length < charactereLimit) {
                    onValueChange(newValue)
                }
            } else {
                onValueChange(newValue)
            }
        },
        modifier = modifier.width(width),
        label = {
            Text(
                text = label,
                color = Color.White,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = Color.White,
                fontFamily = InterFontFamily,
                fontStyle = FontStyle.Italic
            )
        },
        textStyle = TextStyle(
            color = PrimaryColor,
            fontFamily = InterFontFamily,
            fontSize = 18.sp
        ),
        colors = colors,
        singleLine = singleLine,
        readOnly = readOnly,
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        isError = isError,
    )
    if(charactereCounter != null && charactereCounter) {
        Row(
            modifier = Modifier.width(width),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${value.length} / $charactereLimit",
                modifier = Modifier.padding(end = 8.dp),
                fontFamily = InterFontFamily,
                fontSize = 14.sp
            )
        }
    }
    errorMessage?.let {
        CustomErrorMessage(
            errorMessage = errorMessage,
            width = width
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomTextFieldPreview() {
    ConfectionaryAdminTheme {
        var email by remember {mutableStateOf("") }
        var password by remember {mutableStateOf("") }

        Column(
            modifier = Modifier
                .width(500.dp)
                .height(500.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomTextField(
                label = "EMAIL",
                placeholder = "Digite seu email",
                imeAction = ImeAction.Next,
                charactereCounter = true,
                charactereLimit = 10,
                errorMessage = null,
                value = email,
                onValueChange = { newValue -> email = newValue  }
            )

            CustomTextField(
                label = "SENHA",
                placeholder = "Digite sua senha",
                imeAction = ImeAction.Done,
                charactereCounter = true,
                charactereLimit = 5,
                errorMessage = "Você deve digitar sua senha.",
                value = password,
                onValueChange = { newValue -> password = newValue  }
            )
        }
    }
}
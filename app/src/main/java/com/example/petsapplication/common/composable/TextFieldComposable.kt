package com.example.petsapplication.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.rounded.Email
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petsapplication.R
import com.example.petsapplication.common.extension.shadow
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp

@Composable
fun NormalTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: String = "",
    @StringRes label: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions {  },
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .shadow(
                offsetX = 2.dp,
                offsetY = 2.dp,
                borderRadius = 6.dp,
            )
            .padding(end = 4.dp)
            .padding(bottom = 6.dp)

    ) {
        TextField(
            singleLine = true,
            value = value,
            placeholder = { Text("${placeholder}")},
            onValueChange = { onValueChanged(it) },
            label = { Text(text = stringResource(id = label))},
            shape = RoundedCornerShape(6.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White
            ),
            modifier = Modifier
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(6.dp))
                .height(55.dp),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}

@Composable
fun TextFieldWithIcon(
    value: String,
    onValueChanged: (String) -> Unit,
    icon: ImageVector,
    @StringRes label: Int,
    shape: Shape,
    shadowBorderRadius: Dp = 6.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions {  },
    modifier: Modifier = Modifier
) {
    Surface(
        shape = shape,
        modifier = modifier
            .shadow(
                offsetX = 2.dp,
                offsetY = 2.dp,
                borderRadius = shadowBorderRadius,
            )
            .padding(end = 4.dp)
            .padding(bottom = 6.dp)

    ) {
        TextField(
            singleLine = true,
            value = value,
            onValueChange = { onValueChanged(it) },
            label = { Text(text = stringResource(id = label))},
            leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
            shape = shape,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = Modifier
                .border(BorderStroke(1.dp, Color.Black), shape = shape)
                .height(50.dp)
        )
    }
}

@Composable
fun EmailField(
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions {  },
    modifier: Modifier = Modifier
) {
    TextFieldWithIcon(
        value = value,
        onValueChanged = { onValueChanged(it) },
        icon = Icons.Rounded.Email,
        label = R.string.enter_your_email,
        shape = RoundedCornerShape(6.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions {  },
    modifier: Modifier = Modifier
) {
    PasswordField(
        value = value,
        onValueChanged = onValueChanged,
        label = R.string.enter_your_password,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}

@Composable
fun RepeatPasswordField(
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions {  },
    modifier: Modifier = Modifier
) {
    PasswordField(
        value = value,
        onValueChanged = onValueChanged,
        label = R.string.reenter_your_password,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChanged: (String) -> Unit,
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions {  },
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon = if(isVisible) painterResource(id = R.drawable.visibility)
        else painterResource(id = R.drawable.visibility_off)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
    
    Surface(
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .shadow(
                offsetX = 2.dp,
                offsetY = 2.dp,
                borderRadius = 6.dp,
            )
            .padding(end = 4.dp)
            .padding(bottom = 6.dp)
    ) {
        TextField(
            singleLine = true,
            value = value,
            onValueChange = { onValueChanged(it) },
            label = { Text(text = stringResource(id = label))},
            leadingIcon = { Icon(imageVector = Icons.Rounded.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(
                        painter = icon,
                        contentDescription = "Visibility",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            shape = RoundedCornerShape(6.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            modifier = Modifier
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(6.dp))
                .height(50.dp)
        )
    }
}
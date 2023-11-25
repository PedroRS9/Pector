package es.ulpgc.pamn.pector.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import es.ulpgc.pamn.pector.R
import es.ulpgc.pamn.pector.ui.theme.DarkViolet

@Composable
fun PectorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    isEmail: Boolean = false,
    isUser: Boolean = false,
){
    var passwordVisibility by remember { mutableStateOf(!isPassword) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        modifier = Modifier
            .background(Color.Transparent)
            .composed { modifier },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon =
        if (isPassword) {
            @Composable { IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(
                    imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "Toggle password visibility"
                )
            } }
        } else if(isEmail){
            @Composable { Icon(
                imageVector = Icons.Filled.Email,
                contentDescription = "Icono de email"
            ) }
        } else if(isUser){
            @Composable { Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Icono de email"
            ) }
        } else if(errorMessage != null){
            @Composable { Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error) }
        } else null,
        isError = (errorMessage != null),
        supportingText = {
            if(errorMessage != null){
                Text(
                    text = errorMessage ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red
                )
            }
        }
    )
}


@Composable
fun PectorButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
){
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier,
        border = BorderStroke(2.dp, Color.White),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = DarkViolet)
    ) {
        Text(text = text)
    }
}

@Composable
fun PectorCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
){
    PectorLabelledCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = CheckboxDefaults.colors(checkmarkColor = Color.White),
        label = label,
        modifier = modifier
    )
}

@Composable
fun PectorLabelledCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    labelColor: Color = Color.White,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Row(
        modifier = modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = colors
        )
        Text(label, color = labelColor)
    }
}

@Composable
fun PectorClickableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
) {
    ClickableText(
        text = AnnotatedString(text),
        onClick = { onClick() },
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontSize = 16.sp
        )
    )
}

@Composable
fun PectorTransparentWelcomeButton(
onClick: () -> Unit,
text: String,
modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(4.dp)),
        border = BorderStroke(1.dp, Color.White),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
fun PectorSocialMediaIcons() {

    val context = LocalContext.current
    val resources = context.resources
    val urlFacebook = resources.getString(R.string.url_facebook)
    val urlTwitter = resources.getString(R.string.url_twitter)
    val urlInstagram = resources.getString(R.string.url_instagram)
    val packageFacebook = resources.getString(R.string.package_facebook)
    val packageTwitter = resources.getString(R.string.package_twitter)
    val packageInstagram = resources.getString(R.string.package_instagram)

    Row(
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { launchAppOrOpenUrl(context, packageFacebook, urlFacebook) } ) {
            Icon(
                painter = painterResource(R.drawable.ic_facebook),
                contentDescription = "Facebook",
                tint = Color.White
            )
        }
        IconButton(
            onClick = { launchAppOrOpenUrl(context, packageTwitter, urlTwitter) } ) {
            Icon(
                painter = painterResource(R.drawable.ic_x),
                contentDescription = "X (Twitter)",
                tint = Color.White
            )
        }
        IconButton(
            onClick = { launchAppOrOpenUrl(context, packageInstagram, urlInstagram) } ) {
            Icon(
                painter = painterResource(R.drawable.ic_instagram),
                contentDescription = "Instagram",
                tint = Color.White
            )
        }
    }
}

private fun launchAppOrOpenUrl(context: Context, packageName: String, url: String) {
    try {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            context.startActivity(intent)
        } else {
            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(fallbackIntent)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}




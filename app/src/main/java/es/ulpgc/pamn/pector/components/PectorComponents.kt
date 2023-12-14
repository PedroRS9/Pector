package es.ulpgc.pamn.pector.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import es.ulpgc.pamn.pector.R
import es.ulpgc.pamn.pector.data.TopScore
import es.ulpgc.pamn.pector.ui.theme.DarkViolet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
fun ErrorDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back button.
                // If you want to disable that functionality, simply use an empty onDismissRequest.
                onDismiss()
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(stringResource(R.string.alertdialog_confirmbutton))
                }
            }
        )
    }
}

@Composable
fun PectorClickableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: TextUnit,
    fontWeight: FontWeight? = null
) {
    ClickableText(
        text = AnnotatedString(text),
        onClick = { onClick() },
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight
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

@Composable
fun PectorProfilePicture(
    userProfileImage: Painter = painterResource(id = R.drawable.default_profile_pic),
    isChangeable: Boolean = false,
    onUploadImageClick: () -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val imageModifier = Modifier
        .size(150.dp)
        .clip(CircleShape)
        .border(2.dp, Color.Gray, CircleShape)
        .clickable { onClick() }
    val iconSize = 22.dp
    val offsetInPx = LocalDensity.current.run { (iconSize / 2).roundToPx() }

    Box(modifier = modifier) {
        Image(
            painter = userProfileImage,
            contentDescription = "Imagen de perfil",
            modifier = imageModifier
        )
        if(isChangeable){
            IconButton(
                onClick = onUploadImageClick,
                modifier = Modifier
                    .size(35.dp)
                    .offset {
                        IntOffset(x = +offsetInPx - 20, y = +offsetInPx - 20)
                    }
                    .clip(CircleShape)
                    .background(Black)
                    .size(iconSize)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Subir foto de perfil",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun PectorLeaderboard(leaderboard: List<TopScore>){
    Box(
        modifier = Modifier
            .size(330.dp, 355.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(DarkViolet),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1)
        ) {
            items(1){
                Text(
                    text = "5 mejores puntuaciones",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(3.dp)
                )
            }
            items(leaderboard.size) { i ->
                val rowUser = leaderboard.get(i)
                Row(
                    modifier = Modifier
                        .padding(6.dp)
                        .padding(start = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre elementos
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val painter = if(rowUser.profilePicture != null){
                        rememberAsyncImagePainter(rowUser.profilePicture!!)
                    } else {
                        painterResource(id = R.drawable.default_profile_pic)
                    }
                    PectorProfilePicture(
                        userProfileImage = painter,
                        modifier = Modifier.size(50.dp)
                    )
                    // Username
                    Text(
                        text = rowUser.username,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    // Spacer entre el nombre y la puntuación
                    Spacer(modifier = Modifier.size(8.dp))
                    // Score
                    Text(
                        text = rowUser.score.toString(),
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
@Composable
fun ExperienceBar(
    xpPercentage: Float
) {
    LinearProgressIndicator(
        modifier = Modifier
            .height(25.dp),
        color = Color(0xFF5EBCF0),
        trackColor = Color.White,
        progress = xpPercentage
    )
}
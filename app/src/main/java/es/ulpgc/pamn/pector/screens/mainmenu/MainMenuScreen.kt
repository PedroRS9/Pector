package es.ulpgc.pamn.pector.screens.mainmenu

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import es.ulpgc.pamn.pector.R
import es.ulpgc.pamn.pector.components.ErrorDialog
import es.ulpgc.pamn.pector.components.PectorClickableText
import es.ulpgc.pamn.pector.components.PectorProfilePicture
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.navigation.AppScreens
import es.ulpgc.pamn.pector.ui.theme.PectorTheme
import java.io.ByteArrayInputStream

@Composable
fun MainMenuScreen(navController: NavController, backStackEntry: NavBackStackEntry, userGlobalConf: UserGlobalConf) {
    val user by userGlobalConf.currentUser.observeAsState()
    val viewModel: MainMenuViewModel = viewModel(backStackEntry)
    val imageState by viewModel.imageState.observeAsState()
    BodyContent(
        navController = navController,
        user = user,
        clearViewModel = { viewModel.clear() },
        loadImage = { user?.let { viewModel.onLoad(it) } },
        imageState = imageState
    )
}

@Composable
fun BodyContent(
    navController: NavController,
    user: User?,
    clearViewModel: () -> Unit = {},
    loadImage: () -> Unit = {},
    imageState: Result?
) {
    val painter = painterResource(id = R.drawable.default_profile_pic)
    var profilePicture by remember { mutableStateOf(painter) }
    val showDialog = remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var imageLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = user) {
        if (!imageLoaded) {
            loadImage()
            imageLoaded = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pectorBackground(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Close icon at the top-right corner
        Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { /* TODO: Handle close action */ }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White
                )
            }
        }

        // User image
        when(imageState) {
            is Result.ImageSuccess -> {
                println("Success!")
                val inputStream = ByteArrayInputStream(imageState.bytes)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                profilePicture = rememberAsyncImagePainter(bitmap)
            }
            is Result.Error -> {
                println("Error!")
                errorMessage = imageState.exception.message ?: "Error desconocido"
                showDialog.value = true
                clearViewModel()
            }
            null -> {}
            else -> {}
        }
        PectorProfilePicture(userProfileImage = profilePicture, onClick = { navController.navigate(route = AppScreens.ProfileScreen.route) } )

        // User name and level
        PectorClickableText(
            text = user?.getName() ?: "Invitado",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            onClick = { navController.navigate(route = AppScreens.ProfileScreen.route) }
        )
        Text(
            text = "Nivel ${user?.getLevel() ?: "-"}",
            fontSize = 20.sp,
            color = Color.White
        )

        // Game buttons grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(6) { index ->
                GameButton(gameName = "juego $index", onInfoClicked = { /* TODO: Handle info action */ }) {
                    // TODO: Handle game button click
                }
            }
        }

        if(showDialog.value){
            ErrorDialog(
                showDialog = showDialog,
                title = "Error",
                message = errorMessage,
                onDismiss = { showDialog.value = false }
            )
        }
    }
}

@Composable
fun GameButton(
    gameName: String,
    onInfoClicked: () -> Unit,
    onGameClicked: () -> Unit
) {
    OutlinedButton(
        shape = RoundedCornerShape(12.dp),
        onClick = onGameClicked,
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(2.dp, Color.White),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = gameName,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onInfoClicked) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Información",
                    tint = Color.White
                )
            }
        }
    }
}



@Preview
@Composable
fun ShowPreview() {
    PectorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BodyContent(
                navController = rememberNavController(),
                user = null,
                imageState = null
            )
        }
    }
}
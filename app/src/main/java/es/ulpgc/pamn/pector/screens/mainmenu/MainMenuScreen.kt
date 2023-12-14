package es.ulpgc.pamn.pector.screens.mainmenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import es.ulpgc.pamn.pector.navigation.BottomNavigationBar
import es.ulpgc.pamn.pector.ui.theme.PectorTheme

@Composable
fun MainMenuScreen(navController: NavController, backStackEntry: NavBackStackEntry, userGlobalConf: UserGlobalConf) {
    val user by userGlobalConf.currentUser.observeAsState()
    val viewModel = MainMenuViewModel(userGlobalConf)
    LaunchedEffect(Unit){
        viewModel.checkIfPictureIsDownloaded()
    }
    val imageState by viewModel.imageState.observeAsState()
    val showDialog = remember { mutableStateOf(false) }
    BackHandler {
        showDialog.value = true
    }
    if (showDialog.value) {
        // Función para mostrar el diálogo
        ConfirmLogoutDialog(showDialog = showDialog, navController)
    }
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        paddingValues /* TODO: Remove this line. paddingValues should be passed as a parameter */
        BodyContent(
            navController = navController,
            user = user,
            imageState = imageState
        )
    }
}

@Composable
fun BodyContent(
    navController: NavController,
    user: User?,
    loadImage: () -> Unit = {},
    imageState: Result?
) {
    val painter = when (imageState) {
        is Result.ImageSuccess -> rememberAsyncImagePainter(imageState.bytes)
        else -> painterResource(id = R.drawable.default_profile_pic)
    }
    val showDialog = remember { mutableStateOf(false) }
    val errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pectorBackground()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PectorProfilePicture(userProfileImage = painter, onClick = { navController.navigate(route = AppScreens.ProfileScreen.route) } )

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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GameButton(gameName = "Pasapalabra",
                    onInfoClicked = { /* TODO: Handle info action */ },
                    onGameClicked = { navController.navigate(AppScreens.PasapalabraLeaderboardScreen.route)}
                )
            }
            item {
                GameButton(gameName = "Muerte súbita", onInfoClicked = { /* TODO: Handle info action */ }) {
                    // TODO: Handle game button click
                }
            }
            item {
                GameButton(gameName = "1vs1", onInfoClicked = { /* TODO: Handle info action */ }) {
                    // TODO: Handle game button click
                }
            }
            item {
                GameButton(gameName = "Test",
                    onInfoClicked = { /* TODO: Handle info action */ },
                    onGameClicked = { navController.navigate(AppScreens.TestLeaderboardScreen.route)}
                )
            }
            item {
                GameButton(gameName = "Crucigrama", onInfoClicked = { /* TODO: Handle info action */ }) {
                    // TODO: Handle game button click
                }
            }
            item {
                GameButton(gameName = "?", onInfoClicked = { /* TODO: Handle info action */ }) {
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

@Composable
fun ConfirmLogoutDialog(showDialog: MutableState<Boolean>, navController: NavController, ) {
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = { Text("Confirmación") },
        text = { Text("¿Deseas cerrar la sesión?") },
        confirmButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                    navController.navigate(route = AppScreens.LoginScreen.route)
                }
            ) {
                Text("Sí")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showDialog.value = false
                }
            ) {
                Text("No")
            }
        }
    )
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
package es.ulpgc.pamn.pector.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.R
import es.ulpgc.pamn.pector.components.ErrorDialog
import es.ulpgc.pamn.pector.components.PectorButton
import es.ulpgc.pamn.pector.components.PectorClickableText
import es.ulpgc.pamn.pector.components.PectorTextField
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.navigation.AppScreens
import es.ulpgc.pamn.pector.ui.theme.PectorTheme

@Composable
fun LoginScreen(navController: NavController, backStackEntry: NavBackStackEntry, userGlobalConf: UserGlobalConf){
    val viewModel: LoginViewModel = viewModel(backStackEntry)
    val loginState by viewModel.loginState.observeAsState()
    Column{
        BodyContent(
            navController = navController,
            onLogin = { username, password -> viewModel.onLogin(username,password) },
            clearErrors = { viewModel.clearError() },
            loginState = loginState,
            userGlobalConf = userGlobalConf
        )
    }
    BackHandler {
        navController.navigate(AppScreens.WelcomeScreen.route)
    }
}
@Composable
fun BodyContent(navController: NavController,
                onLogin:(String, String) -> Unit,
                clearErrors: () -> Unit,
                loginState: Result?,
                userGlobalConf: UserGlobalConf

) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.pectorBackground().pointerInput(Unit){
            detectTapGestures(onTap = {
                focusManager.clearFocus() // Clear focus when user taps outside of a TextField
            })
        },
        verticalArrangement = if(loginState is Result.Loading) Arrangement.Center else Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if(showDialog.value){
            ErrorDialog(
                showDialog = showDialog,
                title = "Error",
                message = errorMessage,
                onDismiss = { showDialog.value = false }
            )
        }
        when(loginState) {
            is Result.LoginSuccess -> {
                userGlobalConf.setUser(loginState.user)
                navController.navigate(AppScreens.MainMenuScreen.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

            }
            is Result.Error -> {
                errorMessage = loginState.exception.message ?: ""
                showDialog.value = true
                clearErrors()
            }
            is Result.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.width(100.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                return
            }
            else -> {}
        }

        Spacer(modifier = Modifier.size(90.dp))
        Image(
            painter = painterResource(R.drawable.pector_logo),
            contentDescription = stringResource(R.string.pector_logo_description),
            modifier = Modifier
                .size(200.dp)
        )
        PectorTextField(
            value = username,
            onValueChange = { username = it },
            label = stringResource(R.string.type_user_email),
            isUser = true
        )
        PectorTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.type_password),
            isPassword = true
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            PectorButton(
                onClick = { onLogin(username, password) },
                text = stringResource(R.string.button_signin),
            )

            PectorClickableText(
                text = stringResource(R.string.forgot_password_text),
                onClick = {},
                modifier = Modifier,
                fontSize = 16.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            PectorClickableText(
                text = stringResource(R.string.no_acc_text),
                onClick = {},
                modifier = Modifier,
                fontSize = 16.sp
            )

            PectorButton(
                onClick = { navController.navigate(route = AppScreens.SignupScreen.route) },
                text = stringResource(R.string.button_signup),
            )
        }

    }
}

@Preview
@Composable
fun ShowPreview(){
    PectorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BodyContent(
                navController = rememberNavController(),
                onLogin = { _,_ ->},
                clearErrors = {},
                loginState = Result.Success(true),
                userGlobalConf = UserGlobalConf()
            )
        }
    }
}
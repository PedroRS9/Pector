package es.ulpgc.pamn.pector.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import es.ulpgc.pamn.pector.R
import es.ulpgc.pamn.pector.components.PectorButton
import es.ulpgc.pamn.pector.components.PectorCheckbox
import es.ulpgc.pamn.pector.components.PectorClickableText
import es.ulpgc.pamn.pector.components.PectorTextField
import es.ulpgc.pamn.pector.data.Result
import es.ulpgc.pamn.pector.extensions.isValidEmail
import es.ulpgc.pamn.pector.extensions.isValidPassword
import es.ulpgc.pamn.pector.extensions.isValidUsername
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.navigation.AppScreens
import es.ulpgc.pamn.pector.signup.ErrorDialog

@Composable
fun LoginScreen(navController: NavController, backStackEntry: NavBackStackEntry){
    Column{
        BodyContent(
            navController = navController
        )
    }
}
@Composable
fun BodyContent(navController: NavController
                // ...
) {
    Column(
        modifier = Modifier.pectorBackground(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.pector_logo),
            contentDescription = "Logo de Pector",
            modifier = Modifier
                .padding(40.dp)
                .size(150.dp)
        )
        PectorTextField(
            value = "",
            onValueChange = {},
            label = stringResource(R.string.type_user),
            isUser = true
        )
        PectorTextField(
            value = "",
            onValueChange = {},
            label = stringResource(R.string.type_password),
            isPassword = true
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(20.dp)
                .padding(start = 30.dp)
        ) {
            PectorButton(
                onClick = lambda@{},
                text = stringResource(R.string.button_signin),
            )

            PectorClickableText(
                text = stringResource(R.string.forgot_password_text),
                onClick = {},
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(20.dp)
                .padding(start = 30.dp)
        ) {
            PectorClickableText(
                text = stringResource(R.string.no_acc_text),
                onClick = {},
                modifier = Modifier.padding(start = 8.dp)
            )

            PectorButton(
                onClick = { navController.navigate(route = AppScreens.SignupScreen.route) },
                text = stringResource(R.string.button_signup),
            )


        }
    }
}

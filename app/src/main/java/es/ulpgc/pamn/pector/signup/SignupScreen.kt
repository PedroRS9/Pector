package es.ulpgc.pamn.pector.signup

import android.annotation.SuppressLint
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController){
    val view = object : SignupView {
        val presenter: SignupPresenter = SignupPresenterImpl(this);

        override fun showErrorMessage(error: String) {
            println("Error! ${error}")
        }

        override fun navigateToHome() {
            println("Navigate to home")
        }
    }
    view.presenter.onSignup("Paco", "pacoprado@gmail.com", "98765");
    Scaffold {
        BodyContent(navController)
    }
}

@Composable
fun BodyContent(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hi navigation")
        Button(onClick = { /* TODO */ }) {
            Text("Navigate")
        }
    }
}


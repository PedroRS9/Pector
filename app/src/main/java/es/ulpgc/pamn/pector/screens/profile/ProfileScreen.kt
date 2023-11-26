package es.ulpgc.pamn.pector.screens.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.ulpgc.pamn.pector.components.PectorProfilePicture
import es.ulpgc.pamn.pector.data.User
import es.ulpgc.pamn.pector.extensions.pectorBackground
import es.ulpgc.pamn.pector.global.UserGlobalConf
import es.ulpgc.pamn.pector.ui.theme.PectorTheme
import coil.compose.rememberAsyncImagePainter
import es.ulpgc.pamn.pector.R
import es.ulpgc.pamn.pector.components.ErrorDialog
import es.ulpgc.pamn.pector.data.Result
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun ProfileScreen(navController: NavController, backStackEntry: NavBackStackEntry, userGlobalConf: UserGlobalConf){
    val user by userGlobalConf.currentUser.observeAsState()
    val viewModel: ProfileViewModel = viewModel(backStackEntry)
    val imageState by viewModel.imageState.observeAsState()
    val updateState by viewModel.updateState.observeAsState()
    BodyContent(
        navController = navController,
        user = user ?: User("Error", "", ""),
        loadImage = { user?.let { viewModel.onLoad(it) } },
        uploadImage = { filename: String, byteArray: ByteArray ->
            viewModel.onChooseImage(filename, byteArray)
        },
        updateUser = { usuario: User ->
            viewModel.onImageUploaded(usuario)
        },
        clearViewModel = { viewModel.clearError()},
        imageState = imageState,
        updateState = updateState
    )
}

@Composable
fun BodyContent(
    navController: NavController,
    user: User,
    loadImage: () -> Unit,
    uploadImage: (String, ByteArray) -> Unit,
    updateUser: (User) -> Unit,
    clearViewModel: () -> Unit,
    imageState: Result?,
    updateState: Result?
){
    val defaultPainter = painterResource(id = R.drawable.default_profile_pic)
    var profilePicturePainter by remember { mutableStateOf(defaultPainter) }
    val showDialog = remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var imageLoaded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier.pectorBackground(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                inputStream?.let { stream ->
                    val bitmap = BitmapFactory.decodeStream(stream)
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    val bytes = outputStream.toByteArray()
                    uploadImage("${user.getName()}.jpg", bytes)
                }
            }
        }
        LaunchedEffect(key1 = user) {
            if (!imageLoaded) {
                loadImage()
                imageLoaded = true
            }
        }
        PectorProfilePicture(userProfileImage = profilePicturePainter, isChangeable = true, modifier = Modifier.padding(20.dp), onUploadImageClick = {
            launcher.launch("image/*")
        })
        Text(text = user.getName(), color = Color.White, fontSize = 30.sp, modifier = Modifier.padding(20.dp))
        Text(text = "Nivel ${user.getLevel()}", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(5.dp))
        ProgressBar(xp = user.getXp())
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .size(330.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF382155)),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "LOGROS",
                color = Color.White,
                fontSize = 25.sp,
                modifier = Modifier.padding(10.dp)
            )
        }

        if(showDialog.value){
            ErrorDialog(
                showDialog = showDialog,
                title = "Error",
                message = errorMessage,
                onDismiss = { showDialog.value = false }
            )
        }

        when(imageState) {
            is Result.ImageSuccess -> {
                println("Success!")
                val inputStream = ByteArrayInputStream(imageState.bytes)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                profilePicturePainter = rememberAsyncImagePainter(bitmap)
                user.setPictureURL("profile-pictures/${user.getName()}.jpg")
                updateUser(user)
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

        when(updateState){
            is Result.Success -> {}
            is Result.Error -> {
                errorMessage = updateState.exception.message ?: "Error desconocido"
                showDialog.value = true
                clearViewModel()
            }
            null -> {}
            else -> {}
        }
    }
}

@Composable
fun ProgressBar(
    xp: Int,
) {
    val progress = xp.coerceIn(0, 100)
    LinearProgressIndicator(
        modifier = Modifier
            .height(25.dp),
        color = Color(0xFF5EBCF0),
        trackColor = Color.White,
        progress = progress / 100f
    )
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
                user = User("PedroRS9", "", "", null, 1, 50),
                imageState = null,
                updateState = null,
                clearViewModel = {},
                loadImage = {},
                uploadImage = { _, _ -> },
                updateUser = { _ -> },
            )
        }
    }
}
package es.ulpgc.pamn.pector.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import es.ulpgc.pamn.pector.ui.theme.PectorGradientBottom
import es.ulpgc.pamn.pector.ui.theme.PectorGradientTop

fun Modifier.pectorBackground(): Modifier = this
    .fillMaxSize()
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                PectorGradientTop,
                PectorGradientBottom
            )
        )
    )
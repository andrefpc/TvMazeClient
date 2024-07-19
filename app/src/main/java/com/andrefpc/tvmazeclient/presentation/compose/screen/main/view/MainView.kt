package com.andrefpc.tvmazeclient.presentation.compose.screen.main.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.presentation.compose.screen.main.MainViewModel
import com.andrefpc.tvmazeclient.presentation.compose.widget.CustomToolbar

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login))
    val progress by animateLottieCompositionAsState(composition)
    val pinState = remember { mutableStateOf("") }
    val pinButtonState by viewModel.pinButtonState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.verifyBiometrics(context)
        viewModel.checkPin()
    }
    Box {
        CustomToolbar(stringResource(id = R.string.app_name))
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 68.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = pinState.value,
                onValueChange = { pinState.value = it },
                label = { Text(stringResource(id = R.string.pin)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { viewModel.onPinClick(context, pinState.value) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = pinButtonState))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(id = R.string.or))
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { viewModel.onBiometricsClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.phone_authentication))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainView()
}
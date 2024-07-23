package com.andrefpc.tvmazeclient.presentation.compose.screen.chooser.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.presentation.compose.screen.chooser.ChooserViewModel
import com.andrefpc.tvmazeclient.presentation.compose.widget.CustomToolbar

@Composable
fun ChooserView(
    modifier: Modifier = Modifier,
    viewModel: ChooserViewModel = hiltViewModel()
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login))
    val progress by animateLottieCompositionAsState(composition)

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
                modifier = Modifier.testTag("Lottie").size(300.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.choose_evaluate),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { viewModel.openMain() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.jetpack_compose))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = stringResource(id = R.string.or))
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { viewModel.openMainXml() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.xml_based))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooserPreview() {
    ChooserView()
}
package com.andrefpc.tvmazeclient.presentation.compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrefpc.tvmazeclient.presentation.compose.theme.Gray
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerView(
    shimmerType: ShimmerType = ShimmerType.SHOW
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shimmer()
    ) {
        when (shimmerType) {
            ShimmerType.SHOW -> ShimmerShowView()
            ShimmerType.PEOPLE -> ShimmerPeopleView()
            ShimmerType.DETAILS -> ShimmerDetailsView()
        }
    }
}

@Composable
fun ShimmerShowView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        for (i in 1..7) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ShimmerPeopleView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        for (i in 1..5) {
            Row {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Gray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Gray)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ShimmerDetailsView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(12.dp))
                .background(Gray)
        )
        Spacer(modifier = Modifier.height(12.dp))
        for (i in 1..7) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


enum class ShimmerType {
    SHOW,
    PEOPLE,
    DETAILS
}

@Preview(showBackground = true)
@Composable
fun ShimmerPreview() {
    ShimmerView()
}
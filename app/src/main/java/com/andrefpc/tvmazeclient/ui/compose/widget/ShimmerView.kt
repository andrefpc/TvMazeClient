package com.andrefpc.tvmazeclient.ui.compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andrefpc.tvmazeclient.ui.compose.main.MainView
import com.andrefpc.tvmazeclient.ui.compose.theme.Gray
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerView() {
    Box(modifier = Modifier.fillMaxSize().shimmer()){
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            for(i in 1..7){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmerPreview() {
    ShimmerView()
}
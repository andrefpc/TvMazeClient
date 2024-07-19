package com.andrefpc.tvmazeclient.presentation.compose.screen.show_details.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.andrefpc.tvmazeclient.domain.model.Cast
import com.andrefpc.tvmazeclient.domain.model.Person

@Composable
fun ShowDetailsCastView(
    cast: Cast,
    onPersonClick: (Person) -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .padding(horizontal = 8.dp)
            .clickable { onPersonClick(cast.person) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = cast.person.image?.original),
                contentDescription = cast.person.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.padding(bottom = 12.dp),
                text = cast.person.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
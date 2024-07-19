package com.andrefpc.tvmazeclient.presentation.compose.screen.favorites.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.domain.model.Show

@Composable
fun FavoritesItemView(show: Show, onClick: (Show) -> Unit, onDelete: (Show) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onClick(show) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(model = show.image?.medium),
                contentDescription = show.name,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(120.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = show.name,
                        fontSize = 18.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = true)
                    )
                    Text(
                        text = show.genres.joinToString(),
                        fontSize = 12.sp,
                        maxLines = 1,
                        lineHeight = 16.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = show.schedule.days.joinToString(),
                        fontSize = 12.sp,
                        maxLines = 1,
                        lineHeight = 16.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = show.schedule.time,
                        fontSize = 12.sp,
                        maxLines = 1,
                        lineHeight = 16.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete Favorite",
                    modifier = Modifier
                        .width(32.dp)
                        .clickable {
                            onDelete(show)
                        }
                )
            }
        }
    }
}
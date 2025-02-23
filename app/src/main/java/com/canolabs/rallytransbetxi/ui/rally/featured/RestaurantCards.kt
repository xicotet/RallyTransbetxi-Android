package com.canolabs.rallytransbetxi.ui.rally.featured

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.canolabs.rallytransbetxi.BuildConfig
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.data.models.responses.PlaceResponse
import com.canolabs.rallytransbetxi.ui.miscellaneous.Shimmer
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily
import com.canolabs.rallytransbetxi.utils.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RestaurantCards(
    restaurants: List<PlaceResponse>,
    onCardClick: (PlaceResponse) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            pageSize = object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int {
                    return ((availableSpace - 2 * pageSpacing) * 0.85f).toInt()
                }
            },
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) { page ->
            val restaurant = restaurants[page]
            val scaleFactor = if (page == pagerState.currentPage) 1f else 0.85f
            val pageHeight = 280.dp * scaleFactor

            // Animate height transition smoothly
            val animatedHeight by animateDpAsState(targetValue = pageHeight, label = "")
            val alpha = if (restaurants[pagerState.currentPage] == restaurants[page]) 1f else 0.8f

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    onClick = {
                        if (restaurants[pagerState.currentPage] == restaurants[page]) {
                            onCardClick(restaurant)
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page)
                            }
                        }
                    },
                    modifier = Modifier
                        .height(animatedHeight)
                        .alpha(alpha),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(32.dp),
                    elevation = if (page == pagerState.currentPage) CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ) else CardDefaults.cardElevation()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy((-30).dp) // Overlap images
                        ) {
                            val images = restaurant.photos?.take(4) // Take up to 4 images

                            images?.forEach { photo ->
                                val photoReference = photo.name.substringAfterLast("/")
                                val photoUrl = getPhotoUrl(photoReference)
                                val widthModifier = if (images.size == 4) Modifier
                                    .fillMaxWidth()
                                    .weight(1f) else Modifier.size(80.dp)

                                val photoPainter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(photoUrl)
                                        .size(Size.ORIGINAL)
                                        .build(),
                                )

                                when (photoPainter.state) {
                                    is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                                        Shimmer { brush ->
                                            Box(
                                                modifier = widthModifier
                                                    .clip(CircleShape)
                                                    .fillMaxHeight()
                                                    .background(brush)
                                            )
                                        }
                                    }

                                    is AsyncImagePainter.State.Success -> {
                                        Image(
                                            painter = photoPainter,
                                            contentDescription = null,
                                            modifier = widthModifier
                                                .clip(CircleShape)
                                                .fillMaxHeight(), // Ensure the image fits the row height
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        }

                        Text(
                            text = restaurant.displayName.text,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                modifier = Modifier.size(24.dp),
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = restaurant.rating?.toString() ?: "N/A",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            restaurant.priceRange?.startPrice?.units?.let {
                                Text(
                                    text = it + " - " + restaurant.priceRange.endPrice?.units + "€",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            val isRestaurantOpened = restaurant.currentOpeningHours?.openNow

                            isRestaurantOpened?.let {
                                Text(
                                    text = if (it) stringResource(id = R.string.restaurant_opened) else stringResource(
                                        id = R.string.restaurant_closed
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 14.sp,
                                    color = if (it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        // Buttons for calling, opening Google Maps
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(restaurant.googleMapsUri)
                                    ).apply {
                                        setPackage("com.google.android.apps.maps")
                                    }
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .weight(2f)
                                    .clip(RoundedCornerShape(50)),
                                contentPadding = PaddingValues(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape) // Circular background for the icon
                                        .background(MaterialTheme.colorScheme.onPrimary)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.google_maps_icon),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        tint = Color.Unspecified // Preserve the original icon colors
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(id = R.string.open_in_google_maps),
                                    modifier = Modifier.basicMarquee(
                                        initialDelayMillis = 1000,
                                    ),
                                    fontFamily = robotoFamily,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Button for calling the restaurant
                            Button(
                                onClick = {
                                    // Call the restaurant with the phone intent
                                    val callIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:${restaurant.nationalPhoneNumber}")
                                    }
                                    context.startActivity(callIntent)
                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(50)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper function to generate photo URL
fun getPhotoUrl(photoReference: String?): String? {
    return photoReference?.let {
        Constants.PHOTO_URL_TEMPLATE.format(it, BuildConfig.MAPS_API_KEY)
    }
}
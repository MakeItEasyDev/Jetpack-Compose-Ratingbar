package com.jetpack.ratingbar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetpack.ratingbar.MainActivity.Companion.initialRating
import com.jetpack.ratingbar.ratingbar.CustomRatingBar
import com.jetpack.ratingbar.ratingbar.RatingBarConfig
import com.jetpack.ratingbar.ratingbar.RatingBarStyle
import com.jetpack.ratingbar.ui.theme.RatingBarTheme

class MainActivity : ComponentActivity() {

    companion object {
        var initialRating = 2.5f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RatingBarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Compose Rating Bar",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        RatingBarView()
                    }
                }
            }
        }
    }
}

@Composable
fun RatingBarView() {
    var rating: Float by rememberSaveable { mutableStateOf(initialRating) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Current Rating Bar Value: $rating",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        CustomRatingBar(
            value = rating,
            onValueChange = {
                rating = it
            },
            onRatingChanged = {
                Log.d("Rating Value", "RatingBarView: $it")
            },
            config = RatingBarConfig()
                .style(RatingBarStyle.HighLighted)
        )
    }
}























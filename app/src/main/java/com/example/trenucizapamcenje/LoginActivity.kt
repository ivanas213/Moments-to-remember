package com.example.trenucizapamcenje

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trenucizapamcenje.ui.theme.TrenuciZaPamcenjeTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrenuciZaPamcenjeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){
        Image(painter = painterResource(id = R.drawable.logovanje_pozadina), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize() )
        Card(modifier = Modifier.align(Alignment.Center), shape = RoundedCornerShape(15.dp), elevation = CardDefaults.cardElevation(12.dp), border = BorderStroke(3.dp, colorResource(
            id = R.color.pink_stroke
        ) ), colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.pink_background))) {
            Text(
                text = "Ovo je sadržaj",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowLoginScreen() {
    TrenuciZaPamcenjeTheme {
        Greeting2("Android")
    }
}
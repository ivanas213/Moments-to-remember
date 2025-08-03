package com.example.trenucizapamcenje

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.trenucizapamcenje.models.Event
import com.example.trenucizapamcenje.ui.theme.DarkGray80
import com.example.trenucizapamcenje.ui.theme.MojaTema

class OffersActivity : ComponentActivity() {

    private var offersState by mutableStateOf<List<Event>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MojaTema {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting3(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun OffersTitle(){
    Text(
        text = Constants.offersTitle,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily(
            Font(R.font.lunasima_bold)
        ),
        color = DarkGray80
    )
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MojaTema {
        Greeting3("Android")
    }
}
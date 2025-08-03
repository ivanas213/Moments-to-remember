package com.example.trenucizapamcenje

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trenucizapamcenje.models.Offer
import com.example.trenucizapamcenje.ui.components.DrawerContent
import com.example.trenucizapamcenje.ui.components.HeaderSection
import com.example.trenucizapamcenje.ui.theme.DarkGray80
import com.example.trenucizapamcenje.ui.theme.MojaTema
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope

class OfferActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPrefs = getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE)

        val gson = Gson()
        val offerJson = sharedPrefs.getString(Constants.prefCurrentOffer, null)

        val offer = if (offerJson != null) {
            gson.fromJson(offerJson, Offer::class.java)
        } else {
            null
        }

        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val isDrawerOpen = drawerState.isOpen
            MojaTema {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(drawerState, scope)
                    },

                    ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(Modifier.fillMaxSize().graphicsLayer {
                            alpha = if (isDrawerOpen) 0.5f else 1f
                        }
                            .blur(radius = if (isDrawerOpen) 12.dp else 0.dp)
                            .clickable(enabled = !isDrawerOpen) {}){
                            Main(innerPadding, drawerState, scope, offer)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MojaTema {
        Greeting("Android")
    }
}
@Composable
fun Main(innerPadding: PaddingValues, drawerState: DrawerState, scope: CoroutineScope, offer: Offer?){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

    )
    {
        item{

            HeaderSection(drawerState, scope)
        }
        item{
            OfferDetails(offer)
        }

    }
}
@Composable
fun OfferDetails(offer: Offer?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = offer?.name + "\n (sala " + offer?.hall?.name + ")",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.lunasima_bold)),
            color = DarkGray80,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = offer?.hall?.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = offer?.hall?.description ?: "",
            fontSize = 16.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Justify
        )


        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = Constants.appointmentConditions,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(Constants.minGuests + offer?.minGuests)
        Text(Constants.maxGuests + offer?.maxGuests)
        Text("${Constants.priceByGuest} ${offer?.price}€")

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = { /* Zakazivanje */ },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(Constants.appoint)
        }


        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

    }
}



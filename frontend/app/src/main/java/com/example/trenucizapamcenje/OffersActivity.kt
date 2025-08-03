package com.example.trenucizapamcenje

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trenucizapamcenje.RetrofitInstance.retrofitInterface
import com.example.trenucizapamcenje.models.Event
import com.example.trenucizapamcenje.models.Offer
import com.example.trenucizapamcenje.ui.components.DrawerContent
import com.example.trenucizapamcenje.ui.components.HeaderSection
import com.example.trenucizapamcenje.ui.theme.DarkGray80
import com.example.trenucizapamcenje.ui.theme.MojaTema
import kotlinx.coroutines.CoroutineScope
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class OffersActivity : ComponentActivity() {

    private var offersState by mutableStateOf<List<Offer>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callOffers: Call<List<Offer>> = retrofitInterface.getOffers();
        callOffers.enqueue(object : retrofit2.Callback<List<Offer>> {
            override fun onResponse(call: Call<List<Offer>>, response: Response<List<Offer>>) {
                offersState = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        enableEdgeToEdge()
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
                            Main(innerPadding, drawerState, scope)
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun Main(innerPadding: PaddingValues, drawerState: DrawerState, scope: CoroutineScope) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) ,
            horizontalAlignment = Alignment.CenterHorizontally,

        )
        {
            item {
                HeaderSection(drawerState, scope)
            }
            item {
                OffersTitle()
            }
            items(offersState) { offer ->
                OfferCard(offer.offerImageUrl, offer.name, offer.hall.name)
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
    fun OffersTitle() {
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

    @Composable
    fun OfferCard(
        imageUrl: String,
        offerName: String,
        hallName: String,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val cardWidth = 250.dp

            AsyncImage(
                model = imageUrl,
                contentDescription = "Offer Image",
                modifier = Modifier
                    .width(cardWidth)
                    .height(320.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .width(cardWidth)
                    .background(Color.White)
                    .padding(vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = offerName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "(sala ${hallName})",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.cormorant_garamond_bold)),
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
            }
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview2() {
        MojaTema {
            Greeting3("Android")
        }
    }
}
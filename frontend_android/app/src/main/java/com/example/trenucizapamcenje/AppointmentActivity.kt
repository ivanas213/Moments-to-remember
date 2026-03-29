package com.example.trenucizapamcenje

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trenucizapamcenje.RetrofitInstance.retrofitInterface
import com.example.trenucizapamcenje.models.Appointment
import com.example.trenucizapamcenje.models.AppointmentRequest
import com.example.trenucizapamcenje.models.Hall
import com.example.trenucizapamcenje.models.Offer
import com.example.trenucizapamcenje.models.User
import com.example.trenucizapamcenje.ui.components.DrawerContent
import com.example.trenucizapamcenje.ui.components.HeaderSection
import com.example.trenucizapamcenje.ui.theme.DarkGray80
import com.example.trenucizapamcenje.ui.theme.DarkPink
import com.example.trenucizapamcenje.ui.theme.MojaTema
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentActivity : ComponentActivity() {
    var offer: Offer? = null;
    var user: User? = null;
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE)
        val offerJson = sharedPrefs.getString(Constants.prefCurrentOffer, null)
        offer = if (offerJson != null) {
            Gson().fromJson(offerJson, Offer::class.java)
        } else {
            null
        }
        val userJson = sharedPrefs.getString(Constants.prefCurrentUser, null);
        user = if (userJson != null){
            Gson().fromJson(userJson, User::class.java)
        }
        else{
            null
        }
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
                            Main(innerPadding, drawerState, scope, offer)
                        }

                    }
                }
            }
        }
    }
    @Composable
    fun Main(innerPadding: PaddingValues, drawerState: DrawerState, scope: CoroutineScope, offer: Offer?){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        )
        {

                HeaderSection(drawerState, scope)
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ){
                AppointmentCard(offer!!)
            }

        }
    }
    @Composable
    fun AppointmentCard(offer: Offer) {


        var guestCount by remember { mutableStateOf("0") }
        var date by remember { mutableStateOf("") }

        val guestCountInt = guestCount.toIntOrNull() ?: 0
        val total = guestCountInt * offer.price
        val context = LocalContext.current
        Card(
            shape = RoundedCornerShape(40.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.zakazivanje_tortica),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = Constants.appointmentStory,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_condensed_extralight))
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )

                Text(text = "${Constants.priceByGuest}  ${offer.price} €")

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Broj gostiju:\n(min ${offer.minGuests} max ${offer.maxGuests})", fontSize = 13.sp, fontFamily = FontFamily(Font(R.font.roboto_regular)))
                    BasicTextField(
                        value = guestCount,
                        onValueChange = { guestCount = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { innerTextField ->
                            Column(modifier = Modifier.width(100.dp)) {
                                innerTextField()
                                Spacer(modifier = Modifier.height(2.dp))
                                HorizontalDivider(
                                    color = Color.LightGray,
                                    thickness = 1.dp
                                )
                            }
                        }
                    )

                }


                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Datum:", fontSize = 13.sp)
                    BasicTextField(
                        value = date,
                        onValueChange = { date = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        ),
                        decorationBox = { innerTextField ->
                            Column(modifier = Modifier.width(100.dp)) {
                                innerTextField()
                                Spacer(modifier = Modifier.height(2.dp))
                                HorizontalDivider(
                                    color = Color.LightGray,
                                    thickness = 1.dp
                                )
                            }
                        }
                    )

                }


                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )

                Text(
                    text = "Ukupno: $total €",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Button(
                    onClick = {
                        val intent  = Intent(context, OfferActivity::class.java)
                        startActivity(intent)
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE8A9C3),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("Otkaži")
                }
                Button(
                    enabled = offer.minGuests < guestCountInt && guestCountInt< offer.maxGuests && !date.isEmpty()
                    ,
                    onClick = {
                        val request = AppointmentRequest(date = date, offer = offer._id, user = user!!._id, guests = guestCount.toInt())
                        val call: Call<Appointment> = retrofitInterface.addToCart(request);
                        call.enqueue(object: Callback<Appointment>{
                            override fun onResponse(
                                call: Call<Appointment?>,
                                response: Response<Appointment?>
                            ) {
                                val intent  = Intent(context, CartActivity::class.java)
                                startActivity(intent)
                            }

                            override fun onFailure(
                                call: Call<Appointment?>,
                                t: Throwable
                            ) {
                                Log.d("Moje poruke greska: ", t.message.toString())

                            }

                        })

                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE8A9C3),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dodaj u korpu")
                }
            }
        }
    }


}


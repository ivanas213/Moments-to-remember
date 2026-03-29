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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trenucizapamcenje.RetrofitInstance.retrofitInterface
import com.example.trenucizapamcenje.models.Appointment
import com.example.trenucizapamcenje.models.Offer
import com.example.trenucizapamcenje.models.User
import com.example.trenucizapamcenje.ui.components.DrawerContent
import com.example.trenucizapamcenje.ui.components.HeaderSection
import com.example.trenucizapamcenje.ui.theme.MojaTema
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import coil.compose.AsyncImage
import com.example.trenucizapamcenje.models.dto.AppointRequest
import java.time.format.DateTimeFormatter

class CartActivity : ComponentActivity() {
    private var cartItems  by mutableStateOf<List<Appointment>>(emptyList())
    private var cartSum by mutableStateOf<Int>(0);
    private var user: User? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE)

        val gson = Gson()
        val userJson = sharedPrefs.getString(Constants.prefCurrentUser, null)

        user = if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
        val call: Call<List<Appointment>> = retrofitInterface.getCart(user!!._id);
        call.enqueue(object : Callback<List<Appointment>>{
            override fun onResponse(
                call: Call<List<Appointment>?>,
                response: Response<List<Appointment>?>
            ) {
                cartItems = response.body() ?:emptyList()
                cartSum = cartItems.sumOf { it.offer.price * it.guests }
                Log.d("Moje poruke items", cartItems.size.toString());
            }

            override fun onFailure(
                call: Call<List<Appointment>?>,
                t: Throwable
            ) {
                
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
                        Box(Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = if (isDrawerOpen) 0.5f else 1f
                            }
                            .blur(radius = if (isDrawerOpen) 12.dp else 0.dp)
                            .clickable(enabled = !isDrawerOpen) {}){
                            Main(innerPadding, drawerState, scope, cartItems)
                        }

                    }
                }
            }
        }
    }
    @Composable
    fun Main(innerPadding: PaddingValues, drawerState: DrawerState, scope: CoroutineScope, cartItems: List<Appointment>?){
        Log.d("Moje poruke it", cartItems?.size.toString());
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        )
        {
            HeaderSection(drawerState, scope)
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                Card( modifier = Modifier
                    .padding(16.dp)
                    .width(300.dp),
                    shape = RoundedCornerShape(40.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ){
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Column{
                            Image(
                                painter = painterResource(R.drawable.zakazivanje_korpa),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(8.dp)
                            )
                            Text(
                                text = "Korpa",
                                fontFamily = FontFamily(Font(R.font.lunasima_bold)),
                                fontSize = 24.sp
                            )
                        }

                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                        thickness = 1.dp,
                        color = Color.Gray)
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cartItems!!) { item ->
                            CartItemRow(item)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        item{
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = cartSum.toString() + "€",
                                    fontSize = 36.sp,
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                        item{
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                                Button(
                                    onClick = {
                                        val callAppoint: Call<Unit> = retrofitInterface.appoint(
                                            AppointRequest(user!!._id))
                                        callAppoint.enqueue(object : Callback<Unit>{
                                            override fun onResponse(
                                                call: Call<Unit>,
                                                response: Response<Unit>
                                            ) {
                                                val intent  = Intent(context, MainActivity::class.java)
                                                startActivity(intent)
                                            }
                                            override fun onFailure(
                                                call: Call<Unit>,
                                                t: Throwable
                                            ) {
                                                Log.d("Moje poruke greska appoint", t.message.toString())
                                            }

                                        })

                                    },
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFE8A9C3),
                                        contentColor = Color.Black
                                    ),
                                    enabled = cartSum != 0,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                                ) {
                                    Text("Zakaži događaje")
                                }
                            }

                        }
                    }


                }
            }
        }
    }

    @Composable
    fun CartItemRow(
        cartItem: Appointment,
        onRemove: () -> Unit = {}
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 70.dp, height = 120.dp)
                ) {
                    AsyncImage(
                        model = cartItem.offer.offerImageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = cartItem.offer.name,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = Color.Gray
                    )
                    Text(
                        text = "(sala ${cartItem.offer.hall.name})",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = Color.Gray
                    )

                    val dateText = cartItem.date

                    Text(
                        text = "${cartItem.guests} gostiju\n${if (dateText.isNotEmpty()) " $dateText" else ""}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "${cartItem.guests * cartItem.offer.price} €",
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(60.dp)
                        .clickable { onRemove() }
                ) {
                    Image(
                        painter = painterResource(R.drawable.korpa_ukloni),
                        contentDescription = "Ukloni",
                        modifier = Modifier.size(36.dp).clickable{
                            Log.d("Moje poruke korpa ", cartItem.toString())
                            retrofitInterface.deleteAppointment(cartItem._id).enqueue(object:
                                Callback<Unit>{
                                override fun onResponse(
                                    call: Call<Unit?>,
                                    response: Response<Unit?>
                                ) {
                                    Log.d("More poruke korpa", "uklonjeno")
                                    Log.d("More poruke korpa", cartItems.size.toString())
                                    cartItems = cartItems.filter { it._id != cartItem._id }
                                    cartSum -= cartItem.offer.price * cartItem.guests
                                    Log.d("More poruke korpa", cartItems.size.toString())
                                }

                                override fun onFailure(
                                    call: Call<Unit?>,
                                    t: Throwable
                                ) {
                                    Log.d("Moje poruke korpa", "greska neka")
                                }

                            })
                        }
                    )
                    Text("Ukloni", fontSize = 12.sp, color = Color.Black)
                }
            }
        }
    }



}

@Composable
fun CartCard(cartItems: List<Appointment>?){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Card( modifier = Modifier
            .padding(16.dp)
            .width(300.dp),
            shape = RoundedCornerShape(40.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ){
            Column(modifier = Modifier.fillMaxWidth()){
                Image(
                    painter = painterResource(R.drawable.zakazivanje_korpa),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                )
                Text(
                    text = "Korpa",
                    fontFamily = FontFamily(Font(R.font.lunasima_bold)),
                    fontSize = 24.sp
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                    thickness = 1.dp,
                    color = Color.Gray)

            }
        }
    }





}
@Composable
fun Greeting5(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    MojaTema {
        Greeting5("Android")
    }
}

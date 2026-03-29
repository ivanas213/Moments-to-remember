package com.example.trenucizapamcenje

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trenucizapamcenje.RetrofitInstance.retrofitInterface
import com.example.trenucizapamcenje.models.AppointmentRequest
import com.example.trenucizapamcenje.models.Offer
import com.example.trenucizapamcenje.ui.components.DrawerContent
import com.example.trenucizapamcenje.ui.components.HeaderSection
import com.example.trenucizapamcenje.ui.theme.DarkGray80
import com.example.trenucizapamcenje.ui.theme.MojaTema
import com.example.trenucizapamcenje.ui.theme.OrangeStar
import com.example.trenucizapamcenje.models.Rating
import com.example.trenucizapamcenje.models.User
import com.example.trenucizapamcenje.models.dto.AddRatingRequest
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfferActivity : ComponentActivity() {
    var offer: Offer? = null
    var user: User? = null
    private var allRates by mutableStateOf<List<Rating>>(emptyList())
    private var average by mutableDoubleStateOf(0.0);
    @Composable
    private fun LabeledStrongValue(label: String, value: String) {
        Text(
            buildAnnotatedString {
                append(label + " ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(value) }
            },
            fontSize = 16.sp,
            lineHeight = 20.sp
        )
    }
    @Composable
    fun BookingConditionsSection() {
        //HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.6f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Uslovi pri zakazivanju:",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            LabeledStrongValue("Minimalan broj gostiju:", offer!!.minGuests.toString())
            LabeledStrongValue("Maksimalan broj gostiju:", offer!!.maxGuests.toString())
            LabeledStrongValue("Cena po gostu:", "${offer!!.price} €")

            Spacer(Modifier.height(12.dp))
            val context = LocalContext.current
            Button(
                onClick = {
                    val intent = Intent(context, AppointmentActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text("Zakaži svoj termin")
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.6f))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPrefs = getSharedPreferences(Constants.prefName, MODE_PRIVATE)

        val gson = Gson()
        val offerJson = sharedPrefs.getString(Constants.prefCurrentOffer, null)
        offer = offerJson?.let { gson.fromJson(it, Offer::class.java) }

        val userJson = sharedPrefs.getString(Constants.prefCurrentUser, null)
        user = userJson?.let { gson.fromJson(it, User::class.java) }

        refetchRatings()
        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val isDrawerOpen = drawerState.isOpen

            MojaTema {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = { DrawerContent(drawerState, scope) }
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Box(
                            Modifier.fillMaxSize()
                                .graphicsLayer { alpha = if (isDrawerOpen) 0.5f else 1f }
                                .blur(radius = if (isDrawerOpen) 12.dp else 0.dp)
                                .clickable(enabled = !isDrawerOpen) {}
                        ) {
                            Main(innerPadding, drawerState, scope, offer)
                        }
                    }
                }
            }
        }
    }

    private fun refetchRatings() {
        retrofitInterface.getAllRatings(offer!!._id).enqueue(object : Callback<List<Rating>> {
            override fun onResponse(
                call: Call<List<Rating>?>,
                response: Response<List<Rating>?>
            ) {
                allRates = response.body().orEmpty().reversed()
                average = if (allRates.isNotEmpty()) {
                    allRates.sumOf { it.value }.toDouble() / allRates.size
                } else 0.0
                Log.d("Moje poruke average",average.toString());
            }

            override fun onFailure(call: Call<List<Rating>?>, t: Throwable) {
                Log.d("Moje poruke", "All rates greska ${t.message}")
            }
        })
    }
    @Composable
    private fun getAverageRate(rates: List<Rating>): Double {
        return if (rates.isNotEmpty()) allRates.map { it.value }.average() else 0.0
    }

    @Composable
    fun Main(innerPadding: PaddingValues, drawerState: DrawerState, scope: CoroutineScope, offer: Offer?) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { HeaderSection(drawerState, scope) }
            item { OfferDetails(offer) }
            item{
                BookingConditionsSection()
                Spacer(modifier = Modifier.height(12.dp))
            }
            item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RatingStars(rate = average)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = String.format("%.2f", average))
                    }

                    Text(text = "(broj ocena: ${allRates.size})")
                }
            }
            item{
                Spacer(modifier = Modifier.height(12.dp))
                RateSection()
            }
            items(allRates) { rate ->
                Comment(rate)
            }

        }
    }

    @Composable
    fun Stars(rating: Int, onRatingChange: (Int) -> Unit) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            (1..5).forEach { index ->
                val filled = index <= rating
                Image(
                    painter = painterResource(if (filled) R.drawable.zvezdica else R.drawable.zvezdica_siva),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp).clickable { onRatingChange(index) }
                )
            }
        }
    }

    @Composable
    fun RateSection() {
        var myRating by remember { mutableStateOf(0) }
        var text by remember { mutableStateOf("") }
        var isSaving by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = Constants.rate,
                        fontFamily = FontFamily(Font(R.font.lunasima_bold)),
                        fontSize = 20.sp
                    )
                    Stars(rating = myRating, onRatingChange = { myRating = it })
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    placeholder = { Text("Unesi komentar…") },
                    singleLine = false,
                    minLines = 4
                )

                Button(
                    onClick = {
                        isSaving = true
                        val body = AddRatingRequest(
                            user = user!!._id,
                            offer = offer!!._id,
                            value = myRating,
                            comment = text
                        )

                        retrofitInterface.addRating(body).enqueue(object : Callback<Rating> {
                            override fun onResponse(call: Call<Rating>, response: Response<Rating>) {
                                isSaving = false
                                if (response.isSuccessful) {
                                    val created = response.body()
                                    if (created != null) {

                                        average = (average * allRates.size + created.value)/(allRates.size+1)

                                        allRates = allRates.map {
                                            if (it.user._id == created.user._id && it.offer._id == created.offer._id) {
                                                created
                                            } else it
                                        }

                                        if (!allRates.any { it.user._id == created.user._id && it.offer._id == created.offer._id }) {
                                            allRates = listOf(created) + allRates
                                        }

                                    } else {
                                        refetchRatings()
                                    }
                                    myRating = 0
                                    text = ""
                                    focusManager.clearFocus()
                                    Toast.makeText(context, "Ocena sačuvana", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.d("Moje poruke greska: ${response.code()}", response.body().toString())
                                    Toast.makeText(context, "Greška: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Rating>, t: Throwable) {
                                isSaving = false
                                Log.d("Moje poruke greska:", t.message.toString())
                                Toast.makeText(context, "Greška: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
                    enabled = !isSaving && myRating > 0 && text.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(if (isSaving) "Čuvanje..." else Constants.save)
                }
            }
        }
    }

    @Composable
    fun RatingStars(rate: Double) {
        Row {
            repeat(5) { index ->
                val fill = (rate - index).coerceIn(0.0, 1.0)
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.matchParentSize()
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = OrangeStar,
                        modifier = Modifier
                            .matchParentSize()
                            .drawWithContent {
                                val w = size.width * fill
                                clipRect(left = 0f, top = 0f, right = w.toFloat(), bottom = size.height) {
                                    this@drawWithContent.drawContent()
                                }
                            }
                    )
                }
            }
        }
    }

    @Composable
    fun OfferDetails(offer: Offer?) {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
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
                modifier = Modifier.fillMaxWidth().height(200.dp),
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
            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))


            Spacer(modifier = Modifier.height(16.dp))


        }
    }

    @Composable
    fun Comment(rate: Rating) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.moj_profil),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${rate.user.first_name} ${rate.user.last_name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row {
                    repeat(rate.value) {
                        Icon(
                            painter = painterResource(id = R.drawable.zvezdica),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.padding(start = 18.dp),
                text = rate.comment,
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Start
            )
        }
    }

}

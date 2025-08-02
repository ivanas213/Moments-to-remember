package com.example.trenucizapamcenje

import android.R.attr.text
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.trenucizapamcenje.RetrofitInstance.retrofitInterface
import com.example.trenucizapamcenje.models.Event
import com.example.trenucizapamcenje.models.Promotion
import com.example.trenucizapamcenje.ui.theme.DarkGray80
import com.example.trenucizapamcenje.ui.theme.MojaTema
import com.example.trenucizapamcenje.ui.theme.PinkBackground
import com.example.trenucizapamcenje.ui.theme.PinkEventDescription
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.trenucizapamcenje.ui.theme.DarkPinkText
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var promotionsState by mutableStateOf<List<Promotion>>(emptyList())
    private var eventsState by mutableStateOf<List<Event>>(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val callPromotions: Call<List<Promotion>> = retrofitInterface.getPromotions();
        callPromotions.enqueue(object:Callback<List<Promotion>>{
            override fun onResponse(call: Call<List<Promotion>>, response: Response<List<Promotion>>) {
                promotionsState = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Promotion>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
        val callEvents: Call<List<Event>> = retrofitInterface.getEvents();
        callEvents.enqueue(object:Callback<List<Event>>{
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                eventsState = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

        setContent {
            MojaTema {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(innerPadding)
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
    @Composable
    fun Main(innerPadding: PaddingValues){
        val scrollState = rememberScrollState()
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
        {
            item {
                HeaderSection(name = "Android")
            }
            item {
                PromotionsSection(promotionsState)
            }
            item{
                EventTitle()
            }
            items(eventsState) { event ->
                EventCard(event)
            }
            item{
                FooterSection()
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {

        MojaTema {
            // HeaderSection(name: String, modifier: Modifier = Modifier)
            FooterSection()
        }
    }
    @Composable
    fun HeaderSection(name: String, modifier: Modifier = Modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3DCE2))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.meni),
                    contentDescription = "Meni",
                    modifier = Modifier.size(28.dp),
                    tint = Color.DarkGray
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .height(40.dp)
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = "Trenuci za pamćenje",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.italianno_regular)),
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
    @Composable
    fun PromotionsSection(promotions: List<Promotion>) {
        if(promotions.isNotEmpty()){
            var currentIndex by remember { mutableIntStateOf(0) }

            LaunchedEffect(currentIndex) {
                delay(10_000)
                currentIndex = (currentIndex + 1) % promotions.size
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Log.d("Slika", "URL: ${promotions[currentIndex].imageUrl}")
                    Image(
                        painter = rememberAsyncImagePainter(promotions[currentIndex].imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(
                        onClick = {
                            currentIndex = if (currentIndex == 0) promotions.size - 1 else currentIndex - 1
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.strelica_leva),
                            contentDescription = "Leva strelica",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            currentIndex = (currentIndex + 1) % promotions.size
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.strelica_desna),

                            contentDescription = "Desna strelica",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))


            }
        }

    }

    @Composable
    fun EventTitle(){
        Text(
            text = Constants.eventsTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(
                Font(R.font.lunasima_bold)
            ),
            color = DarkGray80
        )
    }
    @Composable
    fun EventsSection(events: List<Event>) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Constants.eventsTitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(
                    Font(R.font.lunasima_bold)
                ),
                color = DarkGray80
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(events) { event ->
                    EventCard(event)
                }
            }
        }
    }
    @Composable
    fun EventCard(event: Event) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(PinkEventDescription)
        ) {
            Image(
                painter = rememberAsyncImagePainter(event.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "(${event.date})",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
    @Composable
    fun FooterSection(){
        val context = LocalContext.current
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = Constants.seeMore,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.lunasima_regular)),
                    color = DarkPinkText,
                    modifier = Modifier.clickable{
                        val intent = Intent(context, EventsActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9DCE2))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.footer_telefon),
                    contentDescription = "Telefon ikona",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = Constants.phoneNumber,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.footer_email),
                    contentDescription = "Email ikona",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = Constants.emailAddress,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.footer_telefon),
                    contentDescription = "Sat ikona",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = Constants.workingTime,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }

    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent()
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Prikaz bočnog menija") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(painter = painterResource(id = R.drawable.meni), contentDescription = "Menu")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                MainContent(Modifier.padding(innerPadding))
            }
        }
        @Composable
        fun DrawerContent() {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
                    .background(Color(0xFFFFEBF0)) // roze pozadina
            ) {
                Text(
                    text = "Trenuci za pamćenje",
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(0xFFEEC1D6)), // tamnija roze traka
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                DrawerItem(icon = Icons.Default.LocalOffer, label = "Ponude")
                DrawerItem(icon = Icons.Default.Notifications, label = "Obaveštenja")
                DrawerItem(icon = Icons.Default.Event, label = "Događaji")
                DrawerItem(icon = Icons.Default.ShoppingCart, label = "Korpa")
                DrawerItem(icon = Icons.Default.Person, label = "Profil")
            }
        }

        @Composable
        fun DrawerItem(icon: ImageVector, label: String) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Navigate or act */ }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(icon, contentDescription = label, tint = Color.Black)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = label, fontSize = 16.sp)
            }
        }

    }

}
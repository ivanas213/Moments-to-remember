package com.example.trenucizapamcenje

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trenucizapamcenje.RetrofitInstance.retrofitInterface
import com.example.trenucizapamcenje.models.Appointment
import com.example.trenucizapamcenje.models.Rating
import com.example.trenucizapamcenje.models.User
import com.example.trenucizapamcenje.ui.components.DrawerContent
import com.example.trenucizapamcenje.ui.components.HeaderSection
import com.example.trenucizapamcenje.ui.theme.DarkGray80
import com.example.trenucizapamcenje.ui.theme.MojaTema
import kotlinx.coroutines.CoroutineScope
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationsActivity : ComponentActivity() {
    private var allNotifications by mutableStateOf<List<Appointment>>(emptyList())
    var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val isDrawerOpen = drawerState.isOpen
            val sharedPrefs = getSharedPreferences(Constants.prefName, MODE_PRIVATE)
            val userJson = sharedPrefs.getString(Constants.prefCurrentUser, null)
            user = userJson?.let { Gson().fromJson(it, User::class.java) }

            val callNotifications: Call<List<Appointment>> = retrofitInterface.getNotifications(user!!._id)
            callNotifications.enqueue(object : Callback<List<Appointment>>{
                override fun onResponse(
                    call: Call<List<Appointment>?>,
                    response: Response<List<Appointment>?>
                ) {
                    allNotifications = response.body().orEmpty().sortedByDescending { it.updatedAt }

                }

                override fun onFailure(
                    call: Call<List<Appointment>?>,
                    t: Throwable
                ) {
                }

            })
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
    fun Main(innerPadding: PaddingValues, drawerState: DrawerState, scope: CoroutineScope){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp) ,
                horizontalAlignment = Alignment.CenterHorizontally,
            )
        {
            item{
                HeaderSection(drawerState, scope)
            }
            item {
                Spacer(Modifier.height(32.dp))
                NotificationsTitle()
                Spacer(Modifier.height(32.dp))
            }
            items(allNotifications) { notification ->
                NotificationCard(notification)
                Spacer(Modifier.height(20.dp))
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview3() {
        MojaTema {
            Greeting4("Android")
        }
    }
    @Composable
    fun NotificationsTitle() {
        Text(
            text = Constants.notificationsTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(
                Font(R.font.lunasima_bold)
            ),
            color = DarkGray80
        )
    }
    @Composable
    fun NotificationCard(
        appointment: Appointment
    ) {
        val isAccepted = appointment.status.equals("accepted")

        val title = if (isAccepted)
            "Zakazivanje je uspešno!"
        else
            "Nažalost, zakazivanje je odbijeno."

        val line2 = if (isAccepted)
            "Vaš događaj za ${appointment.date} je potvrđen od strane organizatora."
        else
            "Vaš događaj za ${appointment.date} je odbijen od strane organizatora."

        Card(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(painter = painterResource(id =  if (isAccepted) R.drawable.notifikacija_prihvaceno else R.drawable.notifikacija_odbijeno), contentDescription = null, modifier = Modifier.size(64.dp))


                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            line2,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = appointment.offer.offerImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 72.dp, height = 92.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "${appointment.offer.name} (${appointment.offer.hall.name})",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${appointment.guests} gostiju, ${appointment.date}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "" + appointment.offer.price * appointment.guests + "€",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }


                }
            }
        }
    }
}


@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

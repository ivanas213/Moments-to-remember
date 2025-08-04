package com.example.trenucizapamcenje

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trenucizapamcenje.models.User
import com.example.trenucizapamcenje.ui.components.DrawerContent
import com.example.trenucizapamcenje.ui.components.HeaderSection
import com.example.trenucizapamcenje.ui.theme.DarkPink
import com.example.trenucizapamcenje.ui.theme.MojaTema
import com.example.trenucizapamcenje.ui.theme.PinkButtonChangeData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import androidx.core.content.edit

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPrefs = getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE)

        val gson = Gson()
        val userJson = sharedPrefs.getString(Constants.prefCurrentUser, null)

        val user = if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
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
                            .clickable(enabled = !isDrawerOpen) {}) {
                            MainUser(innerPadding, drawerState, scope, user)
                        }

                    }
                }
            }
        }
    }


    @Composable
    fun Greeting6(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview5() {
        MojaTema {
        }
    }

    @Composable
    fun MainUser(
        innerPadding: PaddingValues,
        drawerState: DrawerState,
        scope: CoroutineScope,
        user: User?
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        )
        {
            HeaderSection(drawerState, scope)
            ProfileScreen(user)
        }
    }

    @Composable
    fun ProfileScreen(user: User?) {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(40.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .padding(16.dp)
                    .width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = Constants.myProfile,
                        fontSize = 24.sp,
                        fontFamily = FontFamily(
                            Font(R.font.roboto_bold)
                        ),
                        color = DarkPink
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )

                    ProfileRow(Constants.name, user?.first_name ?: "")
                    ProfileRow(Constants.surname, user?.last_name ?: "")
                    ProfileRow(Constants.address, user?.address ?: "")
                    ProfileRow(Constants.contactPhone, user?.phone_number ?: "")

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val intent = Intent(context, ChangeDataActivity::class.java)
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PinkButtonChangeData,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        Text(Constants.changeData)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val intent = Intent(context, ChangePasswordActivity::class.java)
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PinkButtonChangeData,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        Text(Constants.changePassword)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            val sharedPref = context.getSharedPreferences(
                                Constants.prefName,
                                Context.MODE_PRIVATE
                            )
                            sharedPref.edit { clear() }
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.odjava),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = Constants.logout, color = Color.Black)
                    }
                }
            }
        }
    }

    @Composable
    fun ProfileRow(label: String, value: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }


}
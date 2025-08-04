package com.example.trenucizapamcenje

import android.content.Context
import android.content.Context.MODE_PRIVATE
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.trenucizapamcenje.RetrofitInstance.retrofitInterface
import com.example.trenucizapamcenje.models.User
import com.example.trenucizapamcenje.ui.components.DrawerContent
import com.example.trenucizapamcenje.ui.components.HeaderSection
import com.example.trenucizapamcenje.ui.theme.DarkPink
import com.example.trenucizapamcenje.ui.theme.MojaTema
import com.example.trenucizapamcenje.ui.theme.PinkButtonChangeData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class ChangeDataActivity : ComponentActivity() {
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
                            MainUserChangeData(innerPadding, drawerState, scope, user)
                        }

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

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview2() {
        MojaTema {
            Greeting3("Android")
        }
    }

    @Composable
    fun MainUserChangeData(
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
            ProfileChangeDataScreen(user)
        }
    }

    @Composable
    fun ProfileChangeDataScreen(user: User?) {
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
                    var first_name by remember {
                        mutableStateOf(user!!.first_name)
                    }
                    var last_name by remember {
                        mutableStateOf(user!!.last_name)
                    }
                    var address by remember {
                        mutableStateOf(user!!.address)
                    }
                    var phone_number by remember {
                        mutableStateOf(user!!.phone_number)
                    }
                    EditableProfileRow(Constants.name, first_name) { first_name = it }
                    EditableProfileRow(Constants.surname, last_name) { last_name = it }
                    EditableProfileRow(Constants.address, address) { address = it }
                    EditableProfileRow(Constants.contactPhone, phone_number) { phone_number = it }
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PinkButtonChangeData,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        Text(Constants.undo)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val map = HashMap<String, String>()
                            map["first_name"] = first_name
                            map["last_name"] = last_name
                            map["address"] = address
                            map["phone_number"] = phone_number
                            val call: Call<User> = retrofitInterface.updateData(user!!._id, map)
                            call.enqueue(object : retrofit2.Callback<User> {
                                override fun onResponse(
                                    call: Call<User?>,
                                    response: Response<User?>
                                ) {
                                    val sharedPrefs = context.getSharedPreferences(
                                        Constants.prefName,
                                        MODE_PRIVATE
                                    )
                                    sharedPrefs.edit {

                                        val gson = Gson()
                                        val userJson = gson.toJson(response.body())

                                        putString(Constants.prefCurrentUser, userJson)
                                    }
                                    val intent = Intent(context, ProfileActivity::class.java)
                                    context.startActivity(intent)
                                }

                                override fun onFailure(
                                    call: Call<User?>,
                                    t: Throwable
                                ) {
                                    TODO("Not yet implemented")
                                }

                            })
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PinkButtonChangeData,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        Text(Constants.save)
                    }


                }
            }
        }
    }
    @Composable
    fun EditableProfileRow(label: String, text: String, onValueChange: (String) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.padding(start = 8.dp) // malo prostora od labela
                    ) {
                        innerTextField()
                    }
                }
            )
        }
    }
}
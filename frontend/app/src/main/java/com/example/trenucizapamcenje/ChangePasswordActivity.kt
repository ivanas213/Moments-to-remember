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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import retrofit2.Call
import retrofit2.Response

class ChangePasswordActivity : ComponentActivity() {
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
                            MainUserChangePassword(innerPadding, drawerState, scope, user)
                        }

                    }
                }
            }
        }
    }
    @Composable
    fun MainUserChangePassword(
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
            ProfileChangePasswordScreen(user)
        }
    }

    @Composable
    fun ProfileChangePasswordScreen(user: User?) {
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
                        painter = painterResource(id = R.drawable.katanac),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = Constants.titleChangePassword,
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
                    var new_password by remember {
                        mutableStateOf("")
                    }
                    var again by remember {
                        mutableStateOf("")
                    }
                    val isError = new_password != again && again.isNotEmpty()

                    EditableProfileRowPassword(
                        label = Constants.inputPassword,
                        text = new_password,
                        onValueChange = { new_password = it }
                    )
                    EditableProfileRowPassword(
                        label = Constants.inputPasswordAgain,
                        text = again,
                        onValueChange = { again = it }
                    )
                    if (isError) {
                        Text(
                            text = Constants.errorchangingPassword,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(start = 20.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        enabled = !isError,
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
                            map["password"] = new_password
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
    fun EditableProfileRowPassword(
        label: String,
        text: String,
        onValueChange: (String) -> Unit,
        isError: Boolean = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                    BasicTextField(
                        value = text,
                        onValueChange = onValueChange,
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box {
                                innerTextField()
                            }
                        }
                    )
                    androidx.compose.material3.Divider(
                        color = if (isError) Color.Red else Color.Gray,
                        thickness = 1.dp
                    )
                }
            }
        }
    }

}


package com.example.trenucizapamcenje

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.trenucizapamcenje.RetrofitInstance.retrofitInterface
import com.example.trenucizapamcenje.models.User
import com.example.trenucizapamcenje.ui.theme.LoginButtonBackgroundError
import com.example.trenucizapamcenje.ui.theme.MojaTema
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MojaTema{
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var wrongCredentialsError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){
        Image(painter = painterResource(id = R.drawable.logovanje_pozadina), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize() )
        Card(modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 101.dp)
            .fillMaxWidth(0.95f)
            .aspectRatio(377f / 456f)
            .padding(top = 31.dp), shape = RoundedCornerShape(15.dp), elevation = CardDefaults.cardElevation(12.dp), border = BorderStroke(3.dp, colorResource(
            id = R.color.pink_stroke
        ) ), colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.pink_background))) {
            Column (modifier = Modifier
                .fillMaxSize()
                .padding(top = 31.dp),) {
                Text(
                    text = Constants.welcome,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = FontFamily(Font(R.font.inter_28pt_regular)),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black

                    )
                Spacer(modifier = Modifier.height(32.dp))
                Row(modifier = Modifier.padding(start = 52.dp)){
                    Text(text = "Email adresa",
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.inter_28pt_regular)),
                        fontSize = 13.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Icon(
                        painter = painterResource(id = R.drawable.logovanje_korisnicko_ime),
                        contentDescription = "Email ikona",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp, start = 6.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                                        },
                        isError = emailError,
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        placeholder = {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Email adresa",
                                    fontFamily = FontFamily(Font(R.font.inter_28pt_regular)),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },

                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 24.dp),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,


                        )

                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.padding(start = 52.dp)){
                    Text(text = "Lozinka",
                        fontFamily = FontFamily(Font(R.font.inter_28pt_regular)),
                        fontSize = 13.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Icon(
                        painter = painterResource(id = R.drawable.logovanje_lozinka),
                        contentDescription = "Lozinka ikona",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp, start = 6.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it
                                        passwordError = false},
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        isError = passwordError,
                        placeholder = {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Lozinka",
                                    fontFamily = FontFamily(Font(R.font.inter_28pt_regular)),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 24.dp),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,


                        )

                }
                if (emailError or passwordError) {
                    Text(
                        text = Constants.emptyFieldsError,
                        color = LoginButtonBackgroundError,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 72.dp, top = 4.dp)
                    )
                }
                else if(wrongCredentialsError){
                    Text(
                        text = Constants.wrongCredentialsError,
                        color = LoginButtonBackgroundError,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 72.dp, top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        val map = HashMap<String, String>()
                        map["email"]=email
                        map["password"]=password
                        if(email.isBlank() or email.isEmpty()){
                            emailError = true
                        }
                        else if(password.isBlank() or password.isEmpty()){
                            passwordError = true;
                        }
                        else{
                            val call: Call<User> =retrofitInterface.login(map)

                            call.enqueue(object : Callback<User> {
                                override fun onResponse(call: Call<User>, response: Response<User>) {
                                    if (response.isSuccessful && response.body() != null) {
                                        val sharedPrefs = context.getSharedPreferences(Constants.prefName, MODE_PRIVATE)
                                        sharedPrefs.edit {

                                            val gson = Gson()
                                            val userJson = gson.toJson(response.body())

                                            putString(Constants.prefCurrentUser, userJson)
                                        }
                                        val intent = Intent(context, MainActivity::class.java)
                                        context.startActivity(intent)
                                    } else {
//                                        val errorBodyString = response.errorBody()?.string()

//                                        Toast.makeText(
//                                            context,
//                                            errorBodyString ?: "Greška: ${response.code()}",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//
//                                        Log.d("Moje poruke", errorBodyString ?: "Greška: ${response.code()}")
                                        wrongCredentialsError = true;
                                    }
                                }

                                override fun onFailure(call: Call<User>, t: Throwable) {
                                    Toast.makeText(
                                        context,
                                        t.localizedMessage ?: "Mrežna greška",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d("Moje poruke", t.localizedMessage ?: "Mrežna greška")

                                }
                            })


                        }

                    },
                    modifier = Modifier
                        .width(130.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.pink_button))
                ) {
                    Text(
                        text = "Prijavite se",
                        fontFamily = FontFamily(Font(R.font.inter_28pt_regular)),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowLoginScreen() {
    MojaTema {
        Greeting2("Android")
    }
}
package com.example.trenucizapamcenje.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trenucizapamcenje.CartActivity
import com.example.trenucizapamcenje.Constants
import com.example.trenucizapamcenje.EventsActivity
import com.example.trenucizapamcenje.NotificationsActivity
import com.example.trenucizapamcenje.OffersActivity
import com.example.trenucizapamcenje.ProfileActivity
import com.example.trenucizapamcenje.R
import com.example.trenucizapamcenje.ui.theme.MediumGray
import com.example.trenucizapamcenje.ui.theme.PinkEventDescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(drawerState: DrawerState, scope: CoroutineScope) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
            .background(Color(0xFFFFEBF0))
            .padding(top = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3DCE2))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(36.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.meni),
                    contentDescription = "Meni",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            scope.launch {
                                drawerState.close()
                            }
                        },
                    tint = Color.DarkGray
                )
            }
        }
        Column(modifier = Modifier.fillMaxWidth().background(PinkEventDescription),
            horizontalAlignment = Alignment.CenterHorizontally){
            Image(
                painter = painterResource(id = R.drawable.logo_veci),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(end = 8.dp, top = 20.dp)
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = Constants.nameCut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                    fontSize = 36.sp,
                    color = MediumGray,
                    fontFamily = FontFamily(Font(R.font.italianno_regular)),
                    textAlign = TextAlign.Center
                )
            }
        }



        DrawerItem("Ponude", R.drawable.meni_ponude, OffersActivity::class.java)
        DrawerItem("Obaveštenja", R.drawable.meni_obavestenja, NotificationsActivity::class.java)
        DrawerItem("Događaji", R.drawable.meni_dogadjaji, EventsActivity:: class.java)
        DrawerItem("Korpa", R.drawable.meni_korpa, CartActivity:: class.java)
        DrawerItem("Profil", R.drawable.meni_profil, ProfileActivity:: class.java)
    }
}
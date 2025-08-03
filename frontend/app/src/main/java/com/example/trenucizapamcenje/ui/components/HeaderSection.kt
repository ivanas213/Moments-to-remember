package com.example.trenucizapamcenje.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trenucizapamcenje.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HeaderSection(drawerState: DrawerState, scope: CoroutineScope) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3DCE2))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(36.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                            drawerState.open()
                        }
                    },
                tint = Color.DarkGray
            )



            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_veci),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(103.dp)
                        .padding(end = 8.dp)
                )

                Text(
                    text = "Trenuci za pamćenje",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.italianno_regular)),
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
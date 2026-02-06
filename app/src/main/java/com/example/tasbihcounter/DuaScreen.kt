package com.example.tasbihcounter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DuaScreen() {

    val categories = DuaData.categories
    var expandedIndex by remember { mutableStateOf(-1) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF241612), Color(0xFF140C09))
                )
            )
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Daily Dua",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE2C07A)
            )

            Spacer(Modifier.height(20.dp))
        }

        itemsIndexed(categories) { index, category ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        expandedIndex = if (expandedIndex == index) -1 else index
                    },

                shape = RoundedCornerShape(20.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2C1B16)
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = category.name,
                            color = Color(0xFFE2C07A),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )

                        Text(
                            text = if (expandedIndex == index) "▲" else "▼",
                            color = Color(0xFFD0C4BC)
                        )
                    }

                    AnimatedVisibility(visible = expandedIndex == index) {

                        Column {

                            Spacer(Modifier.height(16.dp))

                            category.duas.forEach { dua ->

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                        .background(
                                            Color(0xFF3A241E),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .padding(14.dp)
                                ) {

                                    Text(
                                        dua.title,
                                        color = Color(0xFFE2C07A),
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        dua.arabic,
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        lineHeight = 30.sp
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        dua.transliteration,
                                        color = Color(0xFFD0C4BC),
                                        fontSize = 14.sp
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        dua.meaning,
                                        color = Color.White.copy(0.9f),
                                        fontSize = 14.sp
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        dua.reference,
                                        color = Color(0xFFE2C07A),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

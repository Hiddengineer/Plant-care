package com.example.plantcareapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantcareapp.android.ui.components.VideoPlayer
import com.example.plantcareapp.android.MyApplicationTheme
import Networking.CommandSender
import io.ktor.client.*
import io.ktor.client.engine.android.*
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current

                    var button1Pressed by remember { mutableStateOf(false) }
                    var button2Pressed by remember { mutableStateOf(false) }
                    var button3Pressed by remember { mutableStateOf(false) }
                    var button4Pressed by remember { mutableStateOf(false) }

                    val httpClient = remember { HttpClient(Android) }
                    val commandSender = remember { CommandSender(httpClient) }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VideoPlayer(
                            videoUri = "android.resource://${context.packageName}/raw/plant_video",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .padding(top = 200.dp)
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(200.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ButtonWithIndicator(
                                    buttonText = "Light",
                                    isPressed = button1Pressed,
                                    onButtonClick = {
                                        button1Pressed = !button1Pressed
                                        CoroutineScope(Dispatchers.IO).launch {
                                            commandSender.sendCommand("Light")
                                        }
                                    }
                                )

                                ButtonWithIndicator(
                                    buttonText = "Water",
                                    isPressed = button2Pressed,
                                    onButtonClick = {
                                        button2Pressed = !button2Pressed
                                        CoroutineScope(Dispatchers.IO).launch {
                                            commandSender.sendCommand("Water")
                                        }
                                    }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ButtonWithIndicator(
                                    buttonText = "Auto",
                                    isPressed = button3Pressed,
                                    onButtonClick = {
                                        button3Pressed = !button3Pressed
                                        CoroutineScope(Dispatchers.IO).launch {
                                            commandSender.sendCommand("UpdateTime?sunrise=7&sunset=21")
                                        }
                                    }
                                )

                                ButtonWithIndicator(
                                    buttonText = "Alarm",
                                    isPressed = button4Pressed,
                                    onButtonClick = {
                                        button4Pressed = !button4Pressed
                                        CoroutineScope(Dispatchers.IO).launch {
                                            commandSender.sendCommand("Light") // reuse for alarm
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonWithIndicator(
    buttonText: String,
    isPressed: Boolean,
    onButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(
                    color = if (isPressed) Color.Green else Color.Red,
                    shape = CircleShape
                )
        )

        Button(onClick = onButtonClick) {
            Text(buttonText)
        }
    }
}

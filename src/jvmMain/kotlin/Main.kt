import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
@Preview
fun app() {
    var mode by remember { mutableStateOf(Mode.DECODE_DOL) }
    var enableCache by remember { mutableStateOf(Cache.inEnabled()) }

    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.3F)
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1F)
                ) {

                    AnimatedGradientButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { mode = Mode.DECODE_DOL },
                        text = Mode.DECODE_DOL.value,
                        isSelected = mode == Mode.DECODE_DOL
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedGradientButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { mode = Mode.DECODE_TLV },
                        text = Mode.DECODE_TLV.value,
                        isSelected = mode == Mode.DECODE_TLV
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedGradientButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { mode = Mode.DECODE_TVR },
                        text = Mode.DECODE_TVR.value,
                        isSelected = mode == Mode.DECODE_TVR
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedGradientButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { mode = Mode.DECODE_TSI },
                        text = Mode.DECODE_TSI.value,
                        isSelected = mode == Mode.DECODE_TSI
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider(thickness = 1.dp, color = Color.Black)

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cache"
                        )

                        Spacer(modifier = Modifier.weight(1F))

                        Switch(
                            checked = enableCache,
                            onCheckedChange = { enableCache = it; Cache.switch() },
                            colors = SwitchDefaults.colors(
                                disabledCheckedThumbColor = Color(0xFFF09819),
                                checkedThumbColor = Color(0xFFFF512F)
                            )
                        )
                    }

                }
            }

            Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black))

            Box(
                modifier = Modifier.weight(1F)
            ) {
                ModeSwitcher(mode)
            }
        }
    }
}

@Composable
fun ModeSwitcher(mode: Mode) {
    when (mode) {
        Mode.DECODE_DOL -> DOLDecoder()
        Mode.DECODE_TLV -> TLVDecoder()
        Mode.DECODE_TVR -> TVRDecoder()
        Mode.DECODE_TSI -> TSIDecoder()
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "EMV Tool",
        state = rememberWindowState(width = 1200.dp, height = 600.dp)
    ) {
        app()
    }
}

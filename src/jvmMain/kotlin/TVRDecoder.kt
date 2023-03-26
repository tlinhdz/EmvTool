import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
@Preview
fun TVRDecoder() {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val cache = remember { Cache.getDataFromCache<CacheData.TVRCache>(Mode.DECODE_TVR.value) }
    var tvrData by remember { mutableStateOf("0400248000") }
    var warning by remember { mutableStateOf(false) }
    var tvrResult: ArrayList<ArrayList<TagElement>> by remember { mutableStateOf(arrayListOf()) }

    LaunchedEffect(Unit) {
        cache?.let {
            tvrData = it.tvr
            tvrResult = decodeTVR(tvrData)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("TVR - 5 bytes")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = tvrData,
            onValueChange = { tvrData = it },
            singleLine = false
        )

        if (warning)
            Text(text = warningMessage("Please fill in valid value"))

        Spacer(modifier = Modifier.height(32.dp))

        GradientButton(
            onClick = {
                coroutineScope.launch {
                    if (tvrData.trim().length != 10)
                        warning = true
                    else {
                        warning = false
                        tvrResult = decodeTVR(tvrData)
                        Cache.saveToCache(
                            Mode.DECODE_TVR.value,
                            CacheData.TVRCache(
                                tvr = tvrData.trim()
                            )
                        )
                    }
                }
            },
            text = "Decode"
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (tvrResult.isNotEmpty())
            tvrResult.forEachIndexed { index, item ->
                Text(
                    text = "Byte ${index + 1}",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Table(item)

                Spacer(modifier = Modifier.height(16.dp))
            }
    }
}

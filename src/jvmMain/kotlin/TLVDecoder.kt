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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
@Preview
fun TLVDecoder() {
    val scrollState = rememberScrollState()

    val cache = remember { Cache.getDataFromCache<CacheData.TLVCache>(Mode.DECODE_TLV.value) }
    var tlvData by remember { mutableStateOf("") }
    var value by remember { mutableStateOf(AnnotatedString("")) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        cache?.let {
            tlvData = it.tlv
            value = decodeTLV(tlvData)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("TLV")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = tlvData,
            onValueChange = { tlvData = it },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(32.dp))

        GradientButton(
            onClick = {
                coroutineScope.launch {
                    value = decodeTLV(tlvData.trim())
                    Cache.saveToCache(
                        Mode.DECODE_TLV.value,
                        CacheData.TLVCache(
                            tlv = tlvData.trim()
                        )
                    )
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            },
            text = "Decode"
        )

        Spacer(modifier = Modifier.height(32.dp))

        SelectionContainer {
            Text(
                modifier = Modifier,
                text = value,
                color = Color.Blue
            )
        }
    }
}

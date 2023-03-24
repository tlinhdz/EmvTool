import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
@Preview
fun DOLDecoder() {
    val cache = remember { Cache.getDataFromCache<CacheData.DOLCache>(Mode.DECODE_DOL.value) }
    var dol by remember { mutableStateOf("") }
    var apduData by remember { mutableStateOf("") }
    var value by remember { mutableStateOf(AnnotatedString("")) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        cache?.let {
            dol = it.dol
            apduData = it.data
            value = decodeDOL(dol, apduData)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("DOL")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = dol,
            onValueChange = { dol = it },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("APDU Command's Data")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = apduData,
            onValueChange = { apduData = it },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(32.dp))

        GradientButton(
            onClick = {
                coroutineScope.launch {
                    value = decodeDOL(dol.trim(), apduData.trim())
                    Cache.saveToCache(
                        Mode.DECODE_DOL.value,
                        CacheData.DOLCache(dol = dol.trim(), data = apduData.trim())
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
                text = value
            )
        }
    }
}




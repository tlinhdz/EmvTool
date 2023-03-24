import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.skia.impl.Log

@Composable
@Preview
fun app() {
    var cdol by remember { mutableStateOf("9F02069F03069F1A0295055F2A029A039C019F37049F35019F45029F4C089F34039F21039F7C149F1D08") }
    var apduData by remember { mutableStateOf("00000000000100000000000001440000008000014423032400669B25B9220000000000000000000044030217424700000000000000000000000000000000000000000080000000000000") }
    var value by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.3F)
                    .padding(16.dp),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                ) {
                    Text("C-DOL Decoder")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                ) {
                    Text("TLV Decoder")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                ) {
                    Text("TVR Decoder")
                }
            }

            Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black))

            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text("C-DOL")
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = cdol,
                    onValueChange = { cdol = it },
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

                Button(
                    modifier = Modifier,
                    onClick = { value = decodeCDOL(cdol.trim(), apduData.trim()) }
                ) {
                    Text("Decode")
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("$value")
            }
        }
    }
}

fun decodeCDOL(cdol: String, data: String): String {
    val valueArr: ArrayList<Pair<String, Int>> = arrayListOf()
    var checkSum = 0
    var result = ""
    var tagLengthFormatted = cdol
    var squishedString = data

    if (cdol.isBlank() || squishedString.isBlank())
        return "Please Fill All Fields"
    else {
//        var tag = cdol.squish(2)
//        while (!isValidTag(tag))
//            tag += cdol.squish(2)

        while (tagLengthFormatted.isNotEmpty()) {

            var tag = tagLengthFormatted.take(2)
            tagLengthFormatted = tagLengthFormatted.drop(2)
            while (!isValidTag(tag)) {
                tag += tagLengthFormatted.take(2)
                tagLengthFormatted = tagLengthFormatted.drop(2)
            }

//        cdol.squish(2).toInt().let {
//            valueArr.add(Pair(tag, it))
//            checkSum += it
//        }

            tagLengthFormatted.take(2).toInt(16).let {
                tagLengthFormatted = tagLengthFormatted.drop(2)
                Log.info("$$ ${tag} - ${it}")
                valueArr.add(Pair(tag, it))
                checkSum += it
            }
        }
    }

    if (checkSum == squishedString.length)
        return "Data length and CDOL not match"

    valueArr.forEach {
        result += "${it.first}: ${squishedString.take(it.second * 2)}\n"
        squishedString = squishedString.drop(it.second * 2)
    }

    return result
}

fun isValidTag(tag: String): Boolean {
    tag.toInt(16).let {
        if (
            (tag.length == 2 && (it and 0b00011111) == 0b00011111) ||
            (tag.length > 2 && (it shr 7) xor 0b00000001 == 0)
        )
            return false
    }

    return true
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app()
    }
}


fun String.squish(num: Int): String {
    val result = take(num)
    drop(num)
    return result
}

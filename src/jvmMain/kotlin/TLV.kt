import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import java.lang.Exception

fun decodeDOL(dolStr: String, valueStr: String): AnnotatedString {
    try {

        val tagArray: ArrayList<Pair<String, Int>> = arrayListOf()
        var checkSum = 0
        var result = AnnotatedString("Tags:\n\n")

        val dol = Strings(dolStr)
        val data = Strings(valueStr)

        if (dolStr.isBlank() || data.isBlank()) return warningMessage("Please Fill All Fields")
        else {
            while (dol.isNotEmpty()) {
                var tag = dol.squish(2)
                while (!isValidTag(tag)) {
                    tag += dol.squish(2)
                }

                dol.squish(2).toInt(16).let {
                    tagArray.add(Pair(tag, it))
                    checkSum += it
                }
            }
        }

        if (checkSum == data.length) return warningMessage("Data's length and DOL Tags' length not match")

        tagArray.forEach {
            result += coloredMessage(
                "${it.first} - ${Tags.getTags(it.first)}:\n", Color.Gray
            ) + coloredMessage("${data.squish(it.second * 2)}\n\n")
        }
        return result

    } catch (e: Exception) {
        return warningMessage("Unable To Decode")
    }
}

fun decodeTLV(data: String): AnnotatedString {
    try {

        val tlvData = Strings(data)
        val tagArray: ArrayList<Pair<String, String>> = arrayListOf()
        var result = AnnotatedString("Tags:\n\n")

        if (tlvData.isBlank()) return warningMessage("Please Fill All Fields")
        else {
            while (tlvData.isNotBlank()) {
                var tag = tlvData.squish(2)
                while (!isValidTag(tag)) {
                    tag += tlvData.squish(2)
                }

                val length = tlvData.squish(2).toInt(16)
                val value = tlvData.squish(length * 2)

                tagArray.add(Pair(tag, value))
            }
        }

        tagArray.forEach {
            result += coloredMessage(
                "${it.first} - ${Tags.getTags(it.first)}:\n", Color.Gray
            ) + coloredMessage("${it.second}\n\n")
        }

        return result
    } catch (e: Exception) {
        return warningMessage("Unable To Decode")
    }
}


fun isValidTag(tag: String): Boolean {
    tag.toInt(16).let {
        if (
            (tag.length == 2 && (it and 0b00011111) == 0b00011111) ||
            (tag.length > 2 && (it and 0b10000000) == 0b10000000)
        )
            return false
    }

    return true
}
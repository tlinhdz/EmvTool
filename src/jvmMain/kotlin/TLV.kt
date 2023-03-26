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

        if (checkSum * 2 != data.length) return warningMessage("Data's length and DOL Tags' length not match")

        tagArray.forEach {
            result += coloredMessage(
                "${it.first} - ${Tags.getTagName(it.first)}:\n", Color.Gray
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

        while (tlvData.isNotBlank()) {
            var tag = tlvData.squish(2)
            while (!isValidTag(tag)) {
                tag += tlvData.squish(2)
            }

            val length = tlvData.squish(2).toInt(16)
            val value = tlvData.squish(length * 2)

            tagArray.add(Tags.getTagValue(tag, value))
        }

        tagArray.forEach {
            result += coloredMessage(
                "${it.first} - ${Tags.getTagName(it.first)}:\n", Color.Gray
            ) + coloredMessage("${it.second}\n\n")
        }

        return result
    } catch (e: Exception) {
        return warningMessage("Unable To Decode")
    }
}

fun decodeTVR(tvr: String): ArrayList<ArrayList<TagElement>> {
    try {
        val tvrResult: ArrayList<ArrayList<TagElement>> = arrayListOf()
        val data = Strings(tvr)

        for (i in 0 until 5) {
            data.squish(2).toInt(16).let {
                tvrResult.add(arrayListOf())
                for (j in 0 until 8) {
                    tvrResult.last().add(
                        getBitValue(it, j).let { bitValue ->
                            if (i == 4 && (j == 6 || j == 7))
                                TagElement(
                                    bitValue,
                                    Tags.getTVRBitMeaning(i, j, getBitValueRange(it, 6, 7)),
                                    isLogicElement = true
                                )
                            else
                                TagElement(
                                    bitValue,
                                    Tags.getTVRBitMeaning(i, j, bitValue),
                                )
                        }
                    )
                }
            }
        }

        return tvrResult
    } catch (e: Exception) {
        e.printStackTrace()
        return arrayListOf()
    }
}

fun decodeTSI(tsi: String): ArrayList<ArrayList<TagElement>> {
    try {
        val tsiResult: ArrayList<ArrayList<TagElement>> = arrayListOf()
        val data = Strings(tsi)

        for (i in 0 until 2) {
            data.squish(2).toInt(16).let {
                tsiResult.add(arrayListOf())
                for (j in 0 until 8) {
                    tsiResult.last().add(
                        TagElement(
                            getBitValue(it, j),
                            Tags.getTSIBitMeaning(i, j),
                        )
                    )
                }
            }
        }

        return tsiResult
    } catch (e: Exception) {
        e.printStackTrace()
        return arrayListOf()
    }
}

fun getBitValue(value: Int, index: Int): String {
    (0b00000001 shl (7 - index)).let {
        return if ((it and value) == it) "1" else "0"
    }
}

fun getBitValueRange(value: Int, start: Int, end: Int): String {
    var result = ""
    for (i in start..end) {
        (0b00000001 shl (7 - i)).let {
            result += if ((it and value) == it) "1" else "0"
        }
    }

    return result
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
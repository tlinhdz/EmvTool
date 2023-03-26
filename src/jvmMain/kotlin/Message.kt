import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun warningMessage(msg: String): AnnotatedString {
    return coloredMessage(
        msg = msg,
        color = Color.Red
    )
}

fun coloredMessage(msg: String, color: Color = Color.Black): AnnotatedString {
    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = color
            )
        ) {
            append(msg)
        }
    }
}
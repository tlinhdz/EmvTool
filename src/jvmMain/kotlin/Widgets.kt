import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
@Preview
fun GradientButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(
            modifier = modifier
                .background(
                    shape = RoundedCornerShape(8.dp),
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFF512F),
                            Color(0xFFF09819),
                        )
                    )
                )
                .padding(16.dp),
            text = text,
            color = Color.White
        )
    }
}

@Composable
@Preview
fun AnimatedGradientButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean = true
) {
    val widthAnim by animateFloatAsState(if (isSelected) 1F else 0.6F)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(widthAnim)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFF512F),
                            Color(0xFFF09819),
                        )
                    )
                )
                .padding(16.dp),
            text = text,
            color = Color.White
        )
    }
}

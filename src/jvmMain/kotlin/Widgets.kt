import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview
fun GradientButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
    ) {
        Text(
            modifier = modifier
                .background(
                    shape = RoundedCornerShape(16.dp),
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
    val interactionSource = remember { MutableInteractionSource() }
    val widthAnim by animateFloatAsState(if (isSelected) 1F else 0.75F)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(widthAnim)
                .background(
                    shape = RoundedCornerShape(16.dp),
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
fun Table(
    tagList: ArrayList<TagElement>
) {
    Column {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tagList.forEachIndexed { index, _ ->
                TableElement(
                    modifier = Modifier.weight(1F),
                    text = "B${tagList.size - index}",
                )
            }

            TableElement(
                modifier = Modifier.fillMaxWidth(0.5F),
                text = "Meaning"
            )
        }

        tagList.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tagList.indices.forEach { i ->
                    TableElement(
                        modifier = Modifier.weight(1F),
                        text = if (index == i) item.value else ""
                    )
                }

                TableElement(
                    modifier = Modifier.fillMaxWidth(0.5F),
                    text = item.meaning,
                    isHighlighted = item.value == "1" || item.isLogicElement
                )
            }
        }
    }
}

@Composable
fun TableElement(
    modifier: Modifier,
    text: String,
    isHighlighted: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(
                color = if(isHighlighted) Color.Green else Color.Transparent
            )
            .border(
                border = BorderStroke(1.dp, Color.Gray)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}


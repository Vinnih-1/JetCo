package com.developerstring.jetco_kmp.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco_kmp.charts.barchart.ColumnBarChart
import com.developerstring.jetco_kmp.charts.barchart.ExtendedColumnBarChart
import com.developerstring.jetco_kmp.charts.barchart.GroupColumnBarChart
import com.developerstring.jetco_kmp.charts.barchart.config.BarChartDefaults
import com.developerstring.jetco_kmp.charts.piechart.PieChart
import com.developerstring.jetco_kmp.charts.piechart.config.PieChartDefaults

@Composable
fun ChartsCMPPreview() {

    // The following sample code demonstrates the usage of four different chart components available in this library:
    // 1. Pie Chart: A circular chart divided into slices to illustrate numerical proportions.
    // 2. Column Bar Chart: A vertical bar chart that displays data with rectangular bars representing values.
    // 3. Extended Column Bar Chart: An enhanced version of the Column Bar Chart with additional customization options for UI and state management.
    // 4. Group Column Bar Chart: A chart that displays multiple data series grouped together in a vertical bar format, allowing comparison across different categories.

    val state = rememberScrollState()

    val chartData = mapOf(
        Pair("Test-1", 50f),
        Pair("Test-2", 80f),
        Pair("Test-3.beta", 30f),
        Pair("Test-4", 90f),
        Pair("Test-5", 45f),
        Pair("Test-6.beta", 30f),
        Pair("Test-7", 90f),
        Pair("Test-8", 45f),
        Pair("Test-9", 50f),
        Pair("Test-10", 80f),
        Pair("Test-11", 50f),
        Pair("Test-12", 80f),
    )

    val groupData = mapOf(
        "Q1" to listOf(50f, 80f, 60f),
        "Q2" to listOf(70f, 40f, 90f),
        "Q3" to listOf(90f, 30f, 70f),
        "Q4" to listOf(60f, 90f, 80f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state)
            .padding(top = 60.dp, start = 30.dp, end = 30.dp, bottom = 30.dp)
    ) {

        PieChart(
            modifier = Modifier.fillMaxWidth(),
            chartData = chartData,
            pieChartConfig = PieChartDefaults.pieChartConfig(
                radius = 90.dp,
                isChartItemScrollEnable = false
            ),
            pieChartAnimationConfig = PieChartDefaults.pieChartAnimationConfig(
                animationDuration = 2000
            )
        )

        ColumnBarChart(
            modifier = Modifier,
            chartData = chartData,
            barChartConfig = BarChartDefaults.columnBarChartConfig(
                width = 20.dp,
                shape = CircleShape
            ),
            gridLineStyle = BarChartDefaults.gridLineStyle(
                color = Color(0xFF5700CA)
            ),
        )

        ExtendedColumnBarChart(
            // Modifier to apply layout properties, providing flexibility to define the chart's size and placement
            modifier = Modifier,
            // The data for the column bar chart, typically a list of values or items
            chartData = chartData,
            // The maximum length for the text labels on the X-axis to prevent overflow
            maxTextLengthXAxis = 6,
            // The maximum value for the bars in the chart (used to normalize the bar heights)
            maxBarValue = 100f,
            // Custom label design for the Y-axis scale. The `value` parameter represents the Y-axis label text.
            yAxisScaleLabel = { value ->
                // Card component for styling the Y-axis label with background color and rounded corners
                Card(
                    colors = CardDefaults.cardColors(Color(0xFFCF64FF)), // Set background color of the card
                    shape = RoundedCornerShape(20) // Round the corners of the card
                ) {
                    // Display the text inside the card
                    Text(text = value)
                }
            },
            // Custom popup that appears when a bar is clicked, displaying formatted text based on the bar's value
            barPopUp = { text ->
                // Display the popup with the formatted text using the PopUpLayout component
                PopUpLayout(text = text)
            },
            enableTextRotate = false,
            // Custom popup that appears when an X-axis label is clicked, showing the full text
            labelPopUp = { text ->
                // Display the popup with the clicked label text
                PopUpLayout(text = text)
            },

            // Custom bar design for the chart, with each bar formatted as a card
            barDesign = { text ->

                val value = text.toDoubleOrNull() ?: 0.0
                val formatted = if (value.isNaN() || value.isInfinite()) "0"
                    else if (value % 1 == 0.0) value.toInt().toString()
                    else value.toString()

                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFCF64FF)),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = formatted,
                            modifier = Modifier.rotate(-90f),
                            color = Color.White,
                            maxLines = 1,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        )


        ExtendedColumnBarChart(
            modifier = Modifier,
            chartData = chartData,
            maxTextLengthXAxis = 6,
            maxBarValue = 100f,
            scrollEnable = true,
            yAxisScaleLabel = { value ->
                Card(
                    colors = CardDefaults.cardColors(Color(0xFFCF64FF)),
                    shape = RoundedCornerShape(20)
                ) {
                    Text(text = value)
                }
            },
            barPopUp = { text ->

                PopUpLayout(text = text)
            },
            labelPopUp = { text ->
                PopUpLayout(text = text)
            },
            barDesign = { text ->

                val value = text.toDoubleOrNull() ?: 0.0
                val formatted = if (value.isNaN() || value.isInfinite()) "0"
                    else if (value % 1 == 0.0) value.toInt().toString()
                    else value.toString()

                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFCF64FF)),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = formatted,
                            modifier = Modifier.rotate(-90f),
                            color = Color.White,
                            maxLines = 1,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        )

        ExtendedColumnBarChart(
            modifier = Modifier,

            // The data for the column bar chart, Map<String, Float>
            chartData = chartData,

            // The maximum length for the text labels on the X-axis to prevent overflow
            maxTextLengthXAxis = 6,

            // The maximum value for the bars in the chart (used to normalize the bar heights, defining the y-axis scale).
            maxBarValue = 100f, // If null, it is determined from the chartData. Defaults to null.

            // Custom label design for the Y-axis scale. The `value` parameter represents the Y-axis label text.
            yAxisScaleLabel = { value ->
                // Card component for styling the Y-axis label with background color and rounded corners
                Card(
                    colors = CardDefaults.cardColors(Color(0xFFCF64FF)), // Set background color of the card
                    shape = RoundedCornerShape(20) // Round the corners of the card
                ) {
                    // Display the text inside the card
                    Text(text = value)
                }
            },

            // Custom popup that appears when a bar is clicked, text based on the bar's value
            barPopUp = { text ->
                // Display the popup with the formatted text using the PopUpLayout component
                PopUpLayout(text = text)
            },

            // Custom popup that appears when an X-axis label is clicked, showing the full text
            labelPopUp = { text ->
                // Display the popup with the clicked label text
                PopUpLayout(text = text)
            },

            // Custom bar design for the chart, with each bar formatted as a card
            barDesign = { text ->
                val value = text.toDoubleOrNull() ?: 0.0
                val formatted = if (value.isNaN() || value.isInfinite()) "0"
                    else if (value % 1 == 0.0) value.toInt().toString()
                    else value.toString()

                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFCF64FF)),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = formatted,
                            modifier = Modifier.rotate(-90f),
                            color = Color.White,
                            maxLines = 1,
                            fontSize = 13.sp
                        )
                    }
                }
            },

            // Additional optional parameters to enhance the Extended Column Bar Chart
            // Like: yAxisConfig, xAxisConfig, popUpConfig, animations, textRotateAngle, onBarClicked and more.
            // If custom design is implemented then configuration of that component will no longer be applied.
        )

        GroupColumnBarChart(
            modifier = Modifier
                .fillMaxWidth(),
            // The data for the group column bar chart, Map<String, List<Float>>
            chartData = groupData,
            // The maximum value for the bars in the chart (used to normalize the bar heights, defining the y-axis scale).
            maxBarValue = 100f, // If null, it is determined from the chartData. Defaults to null.
            // Configuration for the group column bar chart, providing options for the bar appearance
            groupBarChartConfig = BarChartDefaults.groupBarChartConfig(
                width = 20.dp, // Set the width of each bar in the group
                shape = RoundedCornerShape(20),
                gapBetweenBar = 2.dp // Space between each bar in the group
            ),
            // Configuration for the grid lines in the background of the chart
            gridLineStyle = BarChartDefaults.gridLineStyle(
                color = Color(0xFFF7F1FF)
            ),
            // Enable or disable text rotation on the x-axis labels.
            enableTextRotate = false
            // Additional optional parameters to enhance the Column Bar Chart
            // Like: yAxisConfig, xAxisConfig, popUpConfig, animations, textRotateAngle, onBarClicked and more.
        )
    }

}

@Composable
fun PopUpLayout(text: String) {

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(100))
            .background(Color(0xFFA8DEFF))
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(100))
                .background(Color(0xFF0091EA)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = text,
                color = Color.White
            )
        }
    }

}

@Composable
fun CurvyWaveLine(
    modifier: Modifier = Modifier,
    pathColor: Color = Color.Black,
    pathWidth: Dp = 2.dp,
    amplitude: Dp = 10.dp,
    numWaves: Int = 30
) {

    val path = remember { Path() }

    path.moveTo(0f, 0f)

    Canvas(
        modifier = modifier
            .clipToBounds()
            .padding(top = pathWidth * 2f)
    ) {

        val waveHeight = amplitude.toPx()
        val waveWidth = size.height / 2

        for (i in 0 until numWaves) {
            val startX = i * waveWidth
            val endX = startX + waveWidth

            path.cubicTo(
                startX + waveWidth / 4, waveHeight,
                startX + 3 * waveWidth / 4, -waveHeight,
                endX, 0f
            )
        }

        drawPath(
            path = path,
            color = pathColor,
            style = Stroke(width = pathWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}
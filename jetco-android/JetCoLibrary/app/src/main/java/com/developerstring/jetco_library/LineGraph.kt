package com.developerstring.jetco_library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco.ui.charts.linegraph.LineGraph
import com.developerstring.jetco.ui.charts.linegraph.MultiLineGraph
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphAnimationConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphAreaFillConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphDefaults
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphGridLineStyle
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphLineConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphPointConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphXAxisConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphYAxisConfig
import com.developerstring.jetco.ui.charts.linegraph.config.LineGraphLiveUpdateConfig
import com.developerstring.jetco.ui.charts.linegraph.model.LineGraphSeries
import com.developerstring.jetco.ui.charts.candlestickchart.CandlestickChart
import com.developerstring.jetco.ui.charts.candlestickchart.config.CandlestickDefaults
import com.developerstring.jetco.ui.charts.candlestickchart.model.CandleChange
import com.developerstring.jetco.ui.charts.candlestickchart.model.CandlestickEntry
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun LineGraphScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
            .padding(vertical = 60.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // 1. Modern Gradient Chart - Vibrant and smooth
        GraphCard(
            title = "Revenue Growth",
            subtitle = "Quarterly performance"
        ) {
            ModernGradientChart()
        }

        // 2. Minimal Clean Chart - Sharp and professional
        GraphCard(
            title = "Website Traffic",
            subtitle = "Daily unique visitors"
        ) {
            MinimalCleanChart()
        }

        // 3. Financial Dashboard Chart - Professional with grid
        GraphCard(
            title = "Stock Performance",
            subtitle = "12-month trend"
        ) {
            FinancialDashboardChart()
        }

        // 4. Dark Theme Chart - Modern dark mode
        GraphCard(
            title = "Server Performance",
            subtitle = "Response time (ms)",
            isDark = true
        ) {
            DarkThemeChart()
        }

        // 5. Multi-Line Comparison - Multiple series
        GraphCard(
            title = "Product Comparison",
            subtitle = "Sales across categories"
        ) {
            MultiLineComparisonChart()
        }

        // 6. Performance Metrics - Percentage growth
        GraphCard(
            title = "Conversion Rate",
            subtitle = "Monthly optimization"
        ) {
            PerformanceMetricsChart()
        }

        // 7. Animated Growth Chart - Bold and dynamic
        GraphCard(
            title = "User Acquisition",
            subtitle = "Exponential growth"
        ) {
            AnimatedGrowthChart()
        }

        // 8. Custom Star Markers - Creative point markers
        GraphCard(
            title = "Premium Features Usage",
            subtitle = "With custom star markers"
        ) {
            CustomStarMarkersChart()
        }

        // 9. Custom Popup - Rich tooltip design
        GraphCard(
            title = "Daily Revenue",
            subtitle = "With enhanced popup"
        ) {
            CustomPopupChart()
        }

        // 10. Live Update Chart - Real-time data with blinking point
        GraphCard(
            title = "Live Temperature Monitor",
            subtitle = "Real-time updates with animation"
        ) {
            LiveUpdateChart()
        }

        // 11. Candlestick Chart - Stock market visualization
        GraphCard(
            title = "Stock Market Analysis",
            subtitle = "OHLC chart with volume"
        ) {
            CandlestickChartExample()
        }
    }
}

@Composable
private fun GraphCard(
    title: String,
    subtitle: String,
    isDark: Boolean = false,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E2E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color(0xFF1E1E2E)
                )
            )
            Text(
                text = subtitle,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = if (isDark) Color(0xFFB0B0C0) else Color(0xFF6B7280)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            content()
        }
    }
}

// 1. Modern Gradient Chart - Vibrant purple-pink gradient
@Composable
private fun ModernGradientChart() {
    val vibrantPurple = Color(0xFF8B5CF6)

    LineGraph(
        chartData = mapOf(
            "Jan" to 45f,
            "Feb" to 52f,
            "Mar" to 48f,
            "Apr" to 65f,
            "May" to 71f,
            "Jun" to 68f,
            "Jul" to 85f,
            "Aug" to 92f
        ),
        chartHeight = 220.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = vibrantPurple,
            lineWidth = 3.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = true,
            brush = Brush.verticalGradient(
                listOf(
                    vibrantPurple.copy(alpha = 0.5f),
                    Color(0xFFEC4899).copy(alpha = 0.2f),
                    Color.Transparent
                )
            )
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 5.dp,
            color = Color.White,
            borderColor = vibrantPurple,
            borderWidth = 3.dp
        ),
        yAxisConfig = LineGraphDefaults.yAxisConfig(
            axisScaleCount = 5,
            textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF6B7280))
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            textStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
        ),
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 1200,
            delayMillis = 100
        ),
        enableGridLines = true,
        gridLineStyle = LineGraphDefaults.gridLineStyle(
            color = Color(0xFFE5E7EB),
            strokeWidth = 1.dp
        ),
        horizontalDrawPadding = 0.dp
    )
}

// 2. Minimal Clean Chart - Simple and sharp
@Composable
private fun MinimalCleanChart() {
    val cleanBlue = Color(0xFF3B82F6)

    LineGraph(
        chartData = mapOf(
            "Mon" to 1200f,
            "Tue" to 1450f,
            "Wed" to 1380f,
            "Thu" to 1620f,
            "Fri" to 1890f,
            "Sat" to 2100f,
            "Sun" to 1750f
        ),
        chartHeight = 200.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = cleanBlue,
            lineWidth = 2.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = false,
            curvature = 0.5f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = false,
            brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 4.dp,
            color = cleanBlue,
            borderColor = Color.White,
            borderWidth = 2.dp
        ),
        yAxisConfig = LineGraphDefaults.yAxisConfig(
            axisScaleCount = 4,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF9CA3AF))
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            isAxisLineEnabled = true,
            axisLineColor = Color(0xFFE5E7EB),
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        enableGridLines = false,
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 800,
            delayMillis = 0
        )
    )
}

// 3. Financial Dashboard Chart - Professional with dollar signs
@Composable
private fun FinancialDashboardChart() {
    val financialGreen = Color(0xFF10B981)

    LineGraph(
        chartData = mapOf(
            "Jan" to 45000f,
            "Feb" to 48000f,
            "Mar" to 52000f,
            "Apr" to 49000f,
            "May" to 58000f,
            "Jun" to 63000f,
            "Jul" to 67000f,
            "Aug" to 71000f,
            "Sep" to 76000f,
            "Oct" to 82000f,
            "Nov" to 88000f,
            "Dec" to 95000f
        ),
        chartHeight = 240.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = financialGreen,
            lineWidth = 2.5.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0.5f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = true,
            brush = Brush.verticalGradient(
                listOf(
                    financialGreen.copy(alpha = 0.3f),
                    Color.Transparent
                )
            )
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 3.dp,
            color = Color.White,
            borderColor = financialGreen,
            borderWidth = 2.dp
        ),
        yAxisConfig = LineGraphYAxisConfig(
            isAxisScaleEnabled = true,
            isAxisLineEnabled = false,
            axisLineWidth = 0.dp,
            axisLineShape = RoundedCornerShape(0.dp),
            axisLineColor = Color.Transparent,
            axisScaleCount = 6,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280)),
            textPrefix = "$",
            textPostfix = "k"
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            textStyle = TextStyle(fontSize = 10.sp, color = Color(0xFF9CA3AF))
        ),
        enableGridLines = true,
        gridLineStyle = LineGraphGridLineStyle(
            color = Color(0xFFF3F4F6),
            strokeWidth = 1.dp,
            dashLength = 8.dp,
            gapLength = 4.dp,
            totalGridLines = 6,
            dashCap = StrokeCap.Round
        ),
        scrollEnabled = true,
        minPointSpacing = 60.dp,
        autoShrinkXAxisLabels = true,
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 1500,
            delayMillis = 200
        )
    )
}

// 4. Dark Theme Chart - Modern dark mode
@Composable
private fun DarkThemeChart() {
    val neonCyan = Color(0xFF06B6D4)

    LineGraph(
        chartData = mapOf(
            "00:00" to 45f,
            "04:00" to 32f,
            "08:00" to 78f,
            "12:00" to 95f,
            "16:00" to 88f,
            "20:00" to 62f
        ),
        chartHeight = 200.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = neonCyan,
            lineWidth = 3.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0.6f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = true,
            brush = Brush.verticalGradient(
                listOf(
                    neonCyan.copy(alpha = 0.4f),
                    Color(0xFF8B5CF6).copy(alpha = 0.1f),
                    Color.Transparent
                )
            )
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 6.dp,
            color = Color(0xFF1E1E2E),
            borderColor = neonCyan,
            borderWidth = 2.5.dp
        ),
        yAxisConfig = LineGraphDefaults.yAxisConfig(
            axisScaleCount = 4,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF94A3B8)),
            textPostfix = "ms"
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF64748B))
        ),
        enableGridLines = true,
        gridLineStyle = LineGraphDefaults.gridLineStyle(
            color = Color(0xFF2D3748),
            strokeWidth = 1.dp
        ),
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 1000,
            delayMillis = 150
        ),
        horizontalDrawPadding = 12.dp
    )
}

// 5. Multi-Line Comparison Chart
@Composable
private fun MultiLineComparisonChart() {
    MultiLineGraph(
        labels = listOf("Q1", "Q2", "Q3", "Q4"),
        seriesList = listOf(
            LineGraphSeries(
                name = "Electronics",
                data = listOf(45f, 62f, 58f, 75f),
                color = Color(0xFF3B82F6)
            ),
            LineGraphSeries(
                name = "Clothing",
                data = listOf(38f, 45f, 52f, 48f),
                color = Color(0xFFEF4444)
            ),
            LineGraphSeries(
                name = "Home & Garden",
                data = listOf(52f, 48f, 65f, 72f),
                color = Color(0xFF10B981)
            )
        ),
        chartHeight = 220.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = Color.Blue,
            lineWidth = 2.5.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0.5f
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 4.dp,
            color = Color.White,
            borderColor = Color.Blue,
            borderWidth = 2.dp
        ),
        enableAreaFill = true,
        yAxisConfig = LineGraphDefaults.yAxisConfig(
            axisScaleCount = 5,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280)),
            textPostfix = "k"
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            textStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium)
        ),
        enableGridLines = true,
        gridLineStyle = LineGraphDefaults.gridLineStyle(
            color = Color(0xFFE5E7EB)
        ),
        enableLegend = true,
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 1300,
            delayMillis = 100
        ),
        horizontalDrawPadding = 16.dp
    )
}

// 6. Performance Metrics Chart - Percentage values
@Composable
private fun PerformanceMetricsChart() {
    val performanceOrange = Color(0xFFF97316)

    LineGraph(
        chartData = mapOf(
            "Jan" to 2.3f,
            "Feb" to 2.8f,
            "Mar" to 3.1f,
            "Apr" to 3.5f,
            "May" to 4.2f,
            "Jun" to 4.8f,
            "Jul" to 5.5f,
            "Aug" to 6.2f
        ),
        chartHeight = 210.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = performanceOrange,
            lineWidth = 3.5.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0.65f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = true,
            brush = Brush.verticalGradient(
                listOf(
                    performanceOrange.copy(alpha = 0.45f),
                    Color(0xFFFBBF24).copy(alpha = 0.15f),
                    Color.Transparent
                )
            )
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 5.dp,
            color = Color.White,
            borderColor = performanceOrange,
            borderWidth = 3.dp
        ),
        yAxisConfig = LineGraphYAxisConfig(
            isAxisScaleEnabled = true,
            isAxisLineEnabled = false,
            axisLineWidth = 0.dp,
            axisLineShape = RoundedCornerShape(0.dp),
            axisLineColor = Color.Transparent,
            axisScaleCount = 4,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280)),
            textPrefix = "",
            textPostfix = "%"
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            isAxisLineEnabled = true,
            axisLineColor = Color(0xFFE5E7EB),
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        enableGridLines = true,
        gridLineStyle = LineGraphDefaults.gridLineStyle(
            color = Color(0xFFF3F4F6)
        ),
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 1400,
            delayMillis = 100
        ),
        horizontalDrawPadding = 10.dp
    )
}

// 7. Animated Growth Chart - Bold and dynamic
@Composable
private fun AnimatedGrowthChart() {
    val boldMagenta = Color(0xFFDB2777)

    LineGraph(
        chartData = mapOf(
            "W1" to 100f,
            "W2" to 150f,
            "W3" to 280f,
            "W4" to 420f,
            "W5" to 680f,
            "W6" to 1050f,
            "W7" to 1580f,
            "W8" to 2200f
        ),
        chartHeight = 230.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = boldMagenta,
            lineWidth = 4.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0.1f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = true,
            brush = Brush.verticalGradient(
                listOf(
                    boldMagenta.copy(alpha = 0.6f),
                    Color(0xFF9333EA).copy(alpha = 0.3f),
                    Color(0xFF3B82F6).copy(alpha = 0.05f),
                    Color.Transparent
                )
            )
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 6.dp,
            color = Color.White,
            borderColor = boldMagenta,
            borderWidth = 3.dp
        ),
        yAxisConfig = LineGraphDefaults.yAxisConfig(
            axisScaleCount = 5,
            textStyle = TextStyle(
                fontSize = 11.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.Medium
            )
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            textStyle = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )
        ),
        enableGridLines = true,
        gridLineStyle = LineGraphDefaults.gridLineStyle(
            color = Color(0xFFE5E7EB),
            strokeWidth = 1.5.dp
        ),
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 2000,
            delayMillis = 300
        ),
        horizontalDrawPadding = 12.dp
    )
}

// 8. Custom Star Markers Chart - Creative custom point markers
@Composable
private fun CustomStarMarkersChart() {
    val starGold = Color(0xFFFBBF24)

    LineGraph(
        chartData = mapOf(
            "Week 1" to 3.5f,
            "Week 2" to 4.2f,
            "Week 3" to 3.8f,
            "Week 4" to 4.9f,
            "Week 5" to 5.3f
        ),
        chartHeight = 220.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = starGold,
            lineWidth = 3.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0.6f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = true,
            brush = Brush.verticalGradient(
                listOf(
                    starGold.copy(alpha = 0.3f),
                    Color.Transparent
                )
            )
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 0.dp,
            color = Color.Transparent,
            borderColor = Color.Transparent,
            borderWidth = 0.dp
        ),
        yAxisConfig = LineGraphDefaults.yAxisConfig(
            axisScaleCount = 5,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        enableGridLines = true,
        gridLineStyle = LineGraphDefaults.gridLineStyle(
            color = Color(0xFFE5E7EB)
        ),
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 1200,
            delayMillis = 100
        ),
        horizontalDrawPadding = 12.dp,
        customPointMarker = { label, value, offset ->
            // Custom star-shaped marker
            Box(
                modifier = Modifier
                    .background(
                        color = starGold,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = "★",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    )
}

// 9. Custom Popup Chart - Enhanced rich tooltip
@Composable
private fun CustomPopupChart() {
    val richBlue = Color(0xFF2563EB)

    LineGraph(
        chartData = mapOf(
            "Mon" to 1250f,
            "Tue" to 1580f,
            "Wed" to 1420f,
            "Thu" to 1890f,
            "Fri" to 2100f,
            "Sat" to 2350f,
            "Sun" to 1980f
        ),
        chartHeight = 220.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = richBlue,
            lineWidth = 3.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0.65f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = true,
            brush = Brush.verticalGradient(
                listOf(
                    richBlue.copy(alpha = 0.4f),
                    Color(0xFF7C3AED).copy(alpha = 0.1f),
                    Color.Transparent
                )
            )
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 5.dp,
            color = Color.White,
            borderColor = richBlue,
            borderWidth = 2.5.dp
        ),
        yAxisConfig = LineGraphDefaults.yAxisConfig(
            axisScaleCount = 5,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280)),
            textPrefix = "$"
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        enableGridLines = true,
        gridLineStyle = LineGraphDefaults.gridLineStyle(
            color = Color(0xFFE5E7EB)
        ),
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 1300,
            delayMillis = 100
        ),
        horizontalDrawPadding = 12.dp,
        customPopup = { label, value, onDismiss ->
            // Custom rich popup
            androidx.compose.ui.window.Popup(
                alignment = androidx.compose.ui.Alignment.Center,
                onDismissRequest = onDismiss
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = richBlue
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = label,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$${"%.2f".format(value)}",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFBBF24)
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Daily Revenue",
                            style = TextStyle(
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }
        }
    )
}

// 10. Live Update Chart - Real-time data with blinking last point
@Composable
private fun LiveUpdateChart() {
    var chartData by remember {
        mutableStateOf(
            mapOf(
                "10:00" to 22.5f,
                "10:05" to 23.2f,
                "10:10" to 22.8f,
                "10:15" to 24.1f,
                "10:20" to 25.3f
            )
        )
    }

    // Simulate live updates
    LaunchedEffect(Unit) {
        val timeLabels = listOf("10:00", "10:05", "10:10", "10:15", "10:20", "10:25", "10:30", "10:35")
        var currentIndex = 5

        while (currentIndex < timeLabels.size) {
            delay(2000) // Update every 2 seconds

            val newValue = Random.nextFloat() * (30f - 20f) + 20f
            val entries = chartData.toMutableMap()

            // Add new entry
            entries[timeLabels[currentIndex]] = newValue

            // Keep only last 5 entries
            if (entries.size > 5) {
                val sortedEntries = entries.entries.sortedBy { timeLabels.indexOf(it.key) }
                chartData = sortedEntries.takeLast(5).associate { it.key to it.value }
            } else {
                chartData = entries
            }

            currentIndex++
            if (currentIndex >= timeLabels.size) currentIndex = 5 // Loop back for demo
        }
    }

    val liveOrange = Color(0xFFFF6B35)

    LineGraph(
        chartData = chartData,
        chartHeight = 220.dp,
        lineConfig = LineGraphLineConfig(
            lineColor = liveOrange,
            lineWidth = 3.dp,
            strokeCap = StrokeCap.Round,
            smoothCurve = true,
            curvature = 0.3f
        ),
        areaFillConfig = LineGraphAreaFillConfig(
            enabled = true,
            brush = Brush.verticalGradient(
                listOf(
                    liveOrange.copy(alpha = 0.3f),
                    Color.Transparent
                )
            )
        ),
        pointConfig = LineGraphPointConfig(
            enabled = true,
            radius = 6.dp,
            color = liveOrange,
            borderColor = Color.White,
            borderWidth = 2.dp
        ),
        liveUpdateConfig = LineGraphLiveUpdateConfig(
            enabled = true,
            blinkEnabled = true,
            blinkDurationMillis = 1000,
            blinkMinRadius = 8f,
            blinkMaxRadius = 20f,
            blinkColor = liveOrange,
            pathTransitionDurationMillis = 800
        ),
        xAxisConfig = LineGraphDefaults.xAxisConfig(
            isAxisScaleEnabled = true,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        yAxisConfig = LineGraphDefaults.yAxisConfig(
            axisScaleCount = 4,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        gridLineStyle = LineGraphGridLineStyle(
            color = Color(0xFFF3F4F6),
            strokeWidth = 1.dp
        ),
        animationConfig = LineGraphAnimationConfig(
            enabled = true,
            durationMillis = 800
        ),
        horizontalDrawPadding = 12.dp,
    )
}

// 11. Candlestick Chart Example - Stock market OHLC chart
@Composable
private fun CandlestickChartExample() {
    // Sample stock data
    val stockData = listOf(
        CandlestickEntry(
            label = "Mon",
            open = 150.0f,
            high = 155.0f,
            low = 148.0f,
            close = 153f,
            volume = 1200000.0f,
            change = CandleChange.Bullish
        ),
        CandlestickEntry(
            label = "Tue",
            open = 153.0f,
            high = 158.0f,
            low = 152.0f,
            close = 156f,
            volume = 1500000.0f,
            change = CandleChange.Bullish
        ),
        CandlestickEntry(
            label = "Wed",
            open = 156.0f,
            high = 157.0f,
            low = 151.0f,
            close = 152.0f,
            volume = 1100000.0f,
            change = CandleChange.Bearish
        ),
        CandlestickEntry(
            label = "Thu",
            open = 152.0f,
            high = 154.0f,
            low = 149.0f,
            close = 151.0f,
            volume = 980000.0f,
            change = CandleChange.Bearish
        ),
        CandlestickEntry(
            label = "Fri",
            open = 151.0f,
            high = 159.0f,
            low = 150.0f,
            close = 158.0f,
            volume = 1800000.0f,
            change = CandleChange.Bullish
        ),
        CandlestickEntry(
            label = "Sat",
            open = 158.0f,
            high = 162.0f,
            low = 157.0f,
            close = 161.0f,
            volume = 1600000.0f,
            change = CandleChange.Bullish
        ),
        CandlestickEntry(
            label = "Sun",
            open = 161.0f,
            high = 163.0f,
            low = 158.0f,
            close = 159.0f,
            volume = 1300000.0f,
            change = CandleChange.Bearish
        )
    )

    CandlestickChart(
        data = stockData,
        modifier = Modifier
            .fillMaxWidth(),
        candleConfig = CandlestickDefaults.candleConfig(
            bullishColor = Color(0xFF10B981),
            bearishColor = Color(0xFFEF4444),
            hollowBullish = false,
            bodyWidthDp = 12.dp,
            wickWidthDp = 1.5.dp
        ),
        volumeConfig = CandlestickDefaults.volumeConfig(
            enabled = true,
            bullishColor = Color(0xFF10B981).copy(alpha = 0.5f),
            bearishColor = Color(0xFFEF4444).copy(alpha = 0.5f),
            heightRatio = 0.25f
        ),
        markerConfig = CandlestickDefaults.markerConfig(
            enabled = true,
            background = Color(0xFF1F2937),
            textStyle = TextStyle(
                fontSize = 12.sp,
                color = Color.White
            )
        ),
        crosshairConfig = CandlestickDefaults.crosshairConfig(
            enabled = true,
            lineColor = Color(0xFF6B7280),
            lineWidthDp = 1.dp
        ),
        yAxisConfig = CandlestickDefaults.yAxisConfig(
            isAxisScaleEnabled = true,
            axisScaleCount = 5,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        xAxisConfig = CandlestickDefaults.xAxisConfig(
            isAxisScaleEnabled = true,
            textStyle = TextStyle(fontSize = 11.sp, color = Color(0xFF6B7280))
        ),
        gridLineStyle = CandlestickDefaults.gridLineStyle(
            color = Color(0xFFF3F4F6),
            strokeWidth = 1.dp
        ),
        animationConfig = CandlestickDefaults.animationConfig(
            enabled = true,
            durationMillis = 1000
        )
    )
}

package com.developerstring.jetco_library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco.ui.components.button.fab.MorphFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.RadialFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.StackFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.model.FabItem
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig.Orientation
import com.developerstring.jetco.ui.components.button.fab.model.MorphFabItem
import com.developerstring.jetco.ui.components.button.fab.model.StackDirection
import com.developerstring.jetco.ui.components.button.fab.model.StackExpandOffset
import com.developerstring.jetco.ui.components.button.fab.model.StackFabItem
import com.developerstring.jetco.ui.components.button.fab.transition.FabButtonTransition
import com.developerstring.jetco.ui.components.button.fab.transition.FabItemTransition

private val Orange  = Color(0xFFE46212)
private val Teal    = Color(0xFF3DBFE2)
private val Green   = Color(0xFF4AA651)
private val Red     = Color(0xFFDE3B3D)
private val Purple  = Color(0xFF7C4DFF)
private val Pink    = Color(0xFFE91E8C)
private val Navy    = Color(0xFF1A237E)
private val Gold    = Color(0xFFFFC107)

private enum class FabVariant(val label: String) {
    RADIAL_CLASSIC("Radial"),
    RADIAL_FULL_ARC("Radial 2"),
    STACK_VERTICAL("Stack 1"),
    STACK_MIXED("Stack 2"),
    STACK_HORIZONTAL("H-Stack"),
    STACK_PUSH("Push"),
    MORPH_GRID("Morph"),
    MORPH_DETAIL("M-Detail")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FloatingActionButtonPreview() {
    var selected by remember { mutableStateOf(FabVariant.STACK_PUSH) }
    var expandOffset by remember { mutableStateOf(StackExpandOffset()) }

    val screenOffsetY by animateDpAsState(
        targetValue = -expandOffset.offsetY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "screenPushY"
    )
    val screenOffsetX by animateDpAsState(
        targetValue = -expandOffset.offsetX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "screenPushX"
    )

    Scaffold(
        floatingActionButton = {
            AnimatedContent(
                targetState = selected,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "fab_switch"
            ) { variant ->
                when (variant) {
                    FabVariant.RADIAL_CLASSIC -> RadialClassicFab()
                    FabVariant.RADIAL_FULL_ARC -> RadialFullArcFab()
                    FabVariant.STACK_VERTICAL -> StackVerticalFab()
                    FabVariant.STACK_MIXED -> StackMixedFab()
                    FabVariant.STACK_HORIZONTAL -> StackHorizontalFab()
                    FabVariant.STACK_PUSH -> StackPushFab(onExpandChange = { expandOffset = it })
                    FabVariant.MORPH_GRID -> MorphGridFab()
                    FabVariant.MORPH_DETAIL -> MorphDetailedFab()
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .offset(y = screenOffsetY, x = screenOffsetX)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items((1..13).toList()) { i ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (i == 0) 72.dp else 56.dp)
                            .background(
                                color = if (i % 3 == 0) Color(0xFFF0F0F0)
                                else if (i % 3 == 1) Color(0xFFE8E8E8)
                                else Color(0xFFDDDDDD),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFCCCCCC), CircleShape)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(10.dp)
                                        .background(Color(0xFFBBBBBB), RoundedCornerShape(4.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(8.dp)
                                        .background(Color(0xFFCCCCCC), RoundedCornerShape(4.dp))
                                )
                            }
                        }
                    }
                }
            }

            FlowRow(
                maxItemsInEachRow = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
            ) {
                FabVariant.entries.forEach { variant ->
                    FilterChip(
                        selected = selected == variant,
                        onClick = { selected = variant },
                        label = {
                            Text(
                                text = variant.label,
                                fontSize = 11.sp,
                                fontWeight = if (selected == variant) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Orange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun MorphDetailedFab() {
    var expanded by remember { mutableStateOf(false) }

    MorphFloatingActionButton(
        expanded = expanded,
        onClick = { expanded = !expanded },
        items = emptyList<MorphFabItem>(),
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(color = Purple, size = 72.dp),
            itemArrangement = FabMainConfig.ItemArrangement(
                morph = Orientation.Morph(
                    columns = 1,
                    width = 260.dp,
                    cardShape = RoundedCornerShape(28.dp)
                )
            )
        ),
        card = {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Purple.copy(alpha = 0.1f), RoundedCornerShape(16.dp)
                        ).clickable(onClick = { expanded = !expanded }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.AccountBox,
                        null,
                        modifier = Modifier.size(32.dp),
                        tint = Purple
                    )
                }
                Text(
                    "User Profile",
                    Modifier.padding(top = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "System Administrator",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Row(
                    Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(Icons.Outlined.Edit, Icons.Outlined.Share, Icons.Outlined.Delete).forEach { icon ->
                        Box(
                            Modifier.size(44.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                icon,
                                null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        },
        modifier = Modifier.offset(x = if (expanded) 20.dp else 0.dp)
    )
}

@Composable
private fun StackHorizontalFab() {
    var expanded by remember { mutableStateOf(false) }

    StackFloatingActionButton(
        expanded = expanded,
        onClick = { expanded = !expanded },
        items = listOf(
            FabItem(icon = Icons.Outlined.Share, onClick = { expanded = false }),
            FabItem(icon = Icons.Outlined.Favorite, onClick = { expanded = false }),
            FabItem(icon = Icons.Outlined.Email, onClick = { expanded = false })
        ),
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(color = Navy, size = 64.dp),
            itemArrangement = FabMainConfig.ItemArrangement(stack = Orientation.Stack(spacedBy = 12.dp)),
            animation = FabMainConfig.Animation(
                enterTransition = FabItemTransition.Slide() + FabItemTransition.Fade(),
                exitTransition = FabItemTransition.Slide() + FabItemTransition.Fade(),
                buttonEnterTransition = FabButtonTransition.Rotate(180f) + FabButtonTransition.Scale(0.7f),
                buttonExitTransition = FabButtonTransition.Rotate(0f) + FabButtonTransition.Scale(1f)
            )
        )
    )
}

@Composable
private fun RadialClassicFab() {
    var expanded by remember { mutableStateOf(false) }

    RadialFloatingActionButton(
        expanded = expanded,
        onClick = { expanded = !expanded },
        items = listOf(
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Home,
                buttonStyle = FabItem.ButtonStyle(color = Orange, size = 56.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.MailOutline,
                buttonStyle = FabItem.ButtonStyle(color = Teal, size = 56.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Place,
                buttonStyle = FabItem.ButtonStyle(color = Green, size = 56.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Delete,
                buttonStyle = FabItem.ButtonStyle(color = Red, size = 56.dp)
            )
        ),
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(color = Orange, size = 72.dp),
            itemArrangement = FabMainConfig.ItemArrangement(
                radial = Orientation.Radial(
                    arc = Orientation.Radial.Arc.END,
                    radius = 100.dp
                )
            ),
            animation = FabMainConfig.Animation(
                enterTransition = FabItemTransition.Spring() + FabItemTransition.Fade(),
                exitTransition  = FabItemTransition.Slide() + FabItemTransition.Fade(),
                enterOrder = FabMainConfig.StaggerOrder.FIFO,
                exitOrder  = FabMainConfig.StaggerOrder.FILO,
                buttonEnterTransition = FabButtonTransition.Rotate(45f) + FabButtonTransition.Scale(0.85f),
                buttonExitTransition  = FabButtonTransition.Rotate(0f) + FabButtonTransition.Scale(1f)
            )
        )
    )
}

@Composable
private fun RadialFullArcFab() {
    var expanded by remember { mutableStateOf(false) }

    RadialFloatingActionButton(
        expanded = expanded,
        onClick = { expanded = !expanded },
        items = listOf(
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Edit,
                buttonStyle = FabItem.ButtonStyle(color = Purple, size = 52.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Share,
                buttonStyle = FabItem.ButtonStyle(color = Pink, size = 52.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Favorite,
                buttonStyle = FabItem.ButtonStyle(color = Red, size = 52.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.AccountBox,
                buttonStyle = FabItem.ButtonStyle(color = Teal, size = 52.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Delete,
                buttonStyle = FabItem.ButtonStyle(color = Gold, size = 52.dp)
            )
        ),
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(
                color = Navy,
                size = 68.dp,
                shape = RoundedCornerShape(20.dp)
            ),
            itemArrangement = FabMainConfig.ItemArrangement(
                radial = Orientation.Radial(
                    arc = Orientation.Radial.Arc.CENTER,
                    radius = 110.dp
                )
            ),
            animation = FabMainConfig.Animation(
                enterTransition = FabItemTransition.Scale() + FabItemTransition.Fade() + FabItemTransition.Rotate(-360f),
                exitTransition  = FabItemTransition.Scale() + FabItemTransition.Fade() + FabItemTransition.Rotate(0f),
                enterOrder = FabMainConfig.StaggerOrder.ALL,
                exitOrder  = FabMainConfig.StaggerOrder.ALL,
                buttonEnterTransition = FabButtonTransition.Rotate(135f) + FabButtonTransition.Scale(0.9f) + FabButtonTransition.SlideTo(y = (-8).dp),
                buttonExitTransition  = FabButtonTransition.Rotate(0f) + FabButtonTransition.Scale(1f) + FabButtonTransition.SlideTo(y = 0.dp)
            )
        )
    )
}

@Composable
private fun StackVerticalFab() {
    var expanded by remember { mutableStateOf(false) }

    StackFloatingActionButton(
        expanded = expanded,
        onClick = { expanded = !expanded },
        items = listOf(
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.AccountBox,
                buttonStyle = FabItem.ButtonStyle(color = Teal, size = 52.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Edit,
                buttonStyle = FabItem.ButtonStyle(color = Purple, size = 52.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Share,
                buttonStyle = FabItem.ButtonStyle(color = Green, size = 52.dp)
            ),
            FabItem(
                onClick = { expanded = false },
                icon = Icons.Outlined.Delete,
                buttonStyle = FabItem.ButtonStyle(color = Red, size = 52.dp)
            )
        ),
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(color = Orange, size = 72.dp),
            itemArrangement = FabMainConfig.ItemArrangement(
                stack = Orientation.Stack(spacedBy = 16.dp)
            ),
            animation = FabMainConfig.Animation(
                enterTransition = FabItemTransition.SlideAndFade() + FabItemTransition.Scale(),
                exitTransition = FabItemTransition.Slide() + FabItemTransition.Fade(),
                enterOrder = FabMainConfig.StaggerOrder.FIFO,
                exitOrder = FabMainConfig.StaggerOrder.FILO,
                buttonEnterTransition = FabButtonTransition.Rotate(45f),
                buttonExitTransition = FabButtonTransition.Rotate(0f)
            )
        )
    )
}

@Composable
private fun StackMixedFab() {
    var expanded by remember { mutableStateOf(false) }

    StackFloatingActionButton(
        expanded = expanded,
        onClick = { expanded = !expanded },
        items = listOf(
            StackFabItem(direction = StackDirection.TOP) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Purple, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("↑1", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            StackFabItem(direction = StackDirection.TOP) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Pink, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("↑2", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            StackFabItem(direction = StackDirection.START) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Teal, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("←1", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            StackFabItem(direction = StackDirection.START) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Green, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("←2", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        ),
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(
                color = Navy,
                size = 68.dp,
                shape = RoundedCornerShape(20.dp)
            ),
            itemArrangement = FabMainConfig.ItemArrangement(
                stack = Orientation.Stack(spacedBy = 14.dp)
            ),
            animation = FabMainConfig.Animation(
                enterTransition = FabItemTransition.Spring() + FabItemTransition.Scale() + FabItemTransition.Fade(),
                exitTransition  = FabItemTransition.Slide() + FabItemTransition.Fade(),
                enterOrder = FabMainConfig.StaggerOrder.FIFO,
                exitOrder = FabMainConfig.StaggerOrder.FILO,
                buttonEnterTransition = FabButtonTransition.Rotate(90f) + FabButtonTransition.Scale(0.8f),
                buttonExitTransition  = FabButtonTransition.Rotate(0f) + FabButtonTransition.Scale(1f)
            )
        )
    )
}

@Composable
private fun StackPushFab(onExpandChange: (StackExpandOffset) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    StackFloatingActionButton(
        expanded = expanded,
        onClick = { expanded = !expanded },
        onExpandChange = onExpandChange,
        items = listOf(
            StackFabItem(direction = StackDirection.TOP) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Purple, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("↑1", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            StackFabItem(direction = StackDirection.TOP) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Purple, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("↑2", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            StackFabItem(direction = StackDirection.TOP) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Purple, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("↑3", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            StackFabItem(direction = StackDirection.START) {
                var text by remember { mutableStateOf("") }

                Box(
                    modifier = Modifier.width(330.dp)
                ) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        placeholder = {
                            Text(text = "Hinted search text", color = Color.Gray)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },
                        shape = RoundedCornerShape(28.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF3F3F9),
                            unfocusedContainerColor = Color(0xFFF3F3F9),
                            disabledContainerColor = Color(0xFFF3F3F9),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }
        ),
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(
                color = Orange,
                size = 64.dp
            ),
            itemArrangement = FabMainConfig.ItemArrangement(
                stack = Orientation.Stack(spacedBy = 14.dp)
            ),
            animation = FabMainConfig.Animation(
                enterTransition = FabItemTransition.Spring() + FabItemTransition.Scale() + FabItemTransition.Fade(),
                exitTransition  = FabItemTransition.Slide() + FabItemTransition.Fade(),
                enterOrder = FabMainConfig.StaggerOrder.FIFO,
                exitOrder  = FabMainConfig.StaggerOrder.FILO,
                buttonEnterTransition = FabButtonTransition.Rotate(45f) + FabButtonTransition.Scale(0.9f),
                buttonExitTransition  = FabButtonTransition.Rotate(0f)  + FabButtonTransition.Scale(1f)
            )
        )
    )
}

@Composable
private fun MorphGridFab() {
    var expanded by remember { mutableStateOf(false) }

    MorphFloatingActionButton(
        expanded = expanded,
        onClick = { expanded = !expanded },
        items = listOf(
            MorphFabItem {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    modifier = Modifier
                        .size(96.dp)
                        .background(Orange, RoundedCornerShape(16.dp))
                ) {
                    Text("🏠", fontSize = 24.sp)
                    Text("Home", color = Color.White, fontSize = 11.sp)
                }
            },
            MorphFabItem {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    modifier = Modifier
                        .size(96.dp)
                        .background(Teal, RoundedCornerShape(16.dp))
                ) {
                    Text("📷", fontSize = 24.sp)
                    Text("Camera", color = Color.White, fontSize = 11.sp)
                }
            },
            MorphFabItem {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    modifier = Modifier
                        .size(96.dp)
                        .background(Purple, RoundedCornerShape(16.dp))
                ) {
                    Text("✏️", fontSize = 24.sp)
                    Text("Edit", color = Color.White, fontSize = 11.sp)
                }
            },
            MorphFabItem {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    modifier = Modifier
                        .size(96.dp)
                        .background(Green, RoundedCornerShape(16.dp))
                ) {
                    Text("🗺️", fontSize = 24.sp)
                    Text("Maps", color = Color.White, fontSize = 11.sp)
                }
            }
        ),
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(color = Orange, size = 72.dp),
            itemArrangement = FabMainConfig.ItemArrangement(
                morph = Orientation.Morph(
                    columns = 2,
                    spacedBy = 12.dp,
                    width = 240.dp,
                    cardShape = RoundedCornerShape(24.dp)
                )
            ),
            animation = FabMainConfig.Animation(
                enterTransition = FabItemTransition.Scale() + FabItemTransition.Fade(),
                enterOrder = FabMainConfig.StaggerOrder.FIFO
            )
        )
    )
}

package com.developerstring.jetco_library

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.components.button.fab.MorphFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.RadialFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.StackFloatingActionButton
import com.developerstring.jetco.ui.components.button.fab.model.FabMainConfig
import com.developerstring.jetco.ui.components.button.fab.model.FabSubItem

private val items = listOf(
    FabSubItem(
        onClick = { println("handle home") },
        icon = Icons.Outlined.Home,
        buttonStyle = FabSubItem.ButtonStyle(
            color = Color(0xFFE46212),
            size = 64.dp
        )
    ),
    FabSubItem(
        onClick = { println("handle mail") },
        icon = Icons.Outlined.MailOutline,
        buttonStyle = FabSubItem.ButtonStyle(
            color = Color(0xFF3DBFE2),
            size = 64.dp
        )
    ),
    FabSubItem(
        onClick = { println("handle place") },
        icon = Icons.Outlined.Place,
        buttonStyle = FabSubItem.ButtonStyle(
            color = Color(0xFF4AA651),
            size = 64.dp
        )
    ),
    FabSubItem(
        onClick = { println("handle delete") },
        icon = Icons.Outlined.Delete,
        buttonStyle = FabSubItem.ButtonStyle(
            color = Color(0xFFDE3B3D),
            size = 64.dp
        )
    )
)

@Composable
fun RadialFloatingActionButtonPreview() {
    var expanded by remember { mutableStateOf(false) }

    RadialFloatingActionButton(
        expanded = expanded,
        items = items,
        onClick = { expanded = !expanded },
        config = FabMainConfig(
            itemArrangement = FabMainConfig.ItemArrangement(
                radius = 144.dp
            ),
            buttonStyle = FabMainConfig.ButtonStyle(
                color = Color(0xFFE46212),
                size = 84.dp
            )
        )
    )
}

@Composable
fun StackFloatingActionButtonPreview() {
    var expanded by remember { mutableStateOf(false) }

    StackFloatingActionButton(
        expanded = expanded,
        items = items,
        onClick = { expanded = !expanded },
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(
                color = Color(0xFFE46212),
                size = 84.dp
            )
        )
    )
}

@Composable
fun MorphFloatingActionButtonPreview() {
    var expanded by remember { mutableStateOf(false) }

    MorphFloatingActionButton(
        expanded = expanded,
        items = listOf(
            FabSubItem(
                onClick = { println("handle home") },
                title = "Home",
                icon = Icons.Outlined.Home,
                buttonStyle = FabSubItem.ButtonStyle(
                    color = Color(0xFFE7722A),
                    shape = RoundedCornerShape(12.dp),
                    size = 100.dp
                ),
                titleStyle = FabSubItem.TitleStyle(
                    weight = FontWeight.Light
                )
            ),
            FabSubItem(
                onClick = { println("handle mail") },
                title = "Mail",
                icon = Icons.Outlined.MailOutline,
                buttonStyle = FabSubItem.ButtonStyle(
                    color = Color(0xFFE7722A),
                    shape = RoundedCornerShape(12.dp),
                    size = 100.dp
                ),
                titleStyle = FabSubItem.TitleStyle(
                    weight = FontWeight.Light
                )
            ),
            FabSubItem(
                onClick = { println("handle place") },
                title = "Place",
                icon = Icons.Outlined.Place,
                buttonStyle = FabSubItem.ButtonStyle(
                    color = Color(0xFFE7722A),
                    shape = RoundedCornerShape(12.dp),
                    size = 100.dp
                ),
                titleStyle = FabSubItem.TitleStyle(
                    weight = FontWeight.Light
                )
            ),
            FabSubItem(
                onClick = { println("handle delete") },
                title = "Delete",
                icon = Icons.Outlined.Delete,
                buttonStyle = FabSubItem.ButtonStyle(
                    color = Color(0xFFE7722A),
                    shape = RoundedCornerShape(12.dp),
                    size = 100.dp
                ),
                titleStyle = FabSubItem.TitleStyle(
                    weight = FontWeight.Light
                )
            )
        ),
        onClick = { expanded = !expanded },
        title = {
            Text(
                text = "Quick Actions",
                color = Color.White
            )
        },
        config = FabMainConfig(
            buttonStyle = FabMainConfig.ButtonStyle(
                color = Color(0xFFE46212),
                size = 84.dp
            )
        )
    )
}
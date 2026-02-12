package com.developerstring.jetco_kmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import com.developerstring.jetco_kmp.preview.AnimatedSearchBarPreview
import com.developerstring.jetco_kmp.preview.CurvedCardPreview
import com.developerstring.jetco_kmp.preview.DateTimePickerScreen
import com.developerstring.jetco_kmp.preview.LineGraphScreen
import com.developerstring.jetco_kmp.preview.StepperPreview
import com.developerstring.jetco_kmp.preview.SwitchButtonKMP
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
//        StepperCompPreview()
//        StepperPreview()
//        SwitchButtonKMP()
//        TicketCardSimple()
//        CurvedCardPreview()
//        TicketCardCustom()

//        AnimatedSearchBarPreview()

        DateTimePickerScreen()
    }
}


package com.canolabs.rallytransbetxi.ui.results

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.canolabs.rallytransbetxi.R
import com.canolabs.rallytransbetxi.domain.entities.RacingCategory
import com.canolabs.rallytransbetxi.ui.theme.PaddingLarge
import com.canolabs.rallytransbetxi.ui.theme.PaddingMedium
import com.canolabs.rallytransbetxi.ui.theme.PaddingSmall
import com.canolabs.rallytransbetxi.ui.theme.robotoFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RacingCategorySegmentedButton(
    selectedRacingCategories: List<RacingCategory>,
    onSelectedTabIndexChange: (Int) -> Unit
) {
    val categories = listOf(R.string.serie, R.string.prototype, R.string.agria)

    MultiChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PaddingLarge, horizontal = PaddingMedium)
    ) {
        categories.forEachIndexed { index, title ->
            SegmentedButton(
                modifier = Modifier,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = categories.size,
                    baseShape = MaterialTheme.shapes.medium
                ),
                checked = selectedRacingCategories.any { it.getTabIndex() == index },
                onCheckedChange = { onSelectedTabIndexChange(index) },
                icon = {},
            ) {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = robotoFamily,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.padding(PaddingSmall)
                )
            }
        }
    }
}
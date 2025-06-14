package com.jean.cuidemonosaqp.shared.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    // Unexpected Strings
    data class DynamicString(val value: String): UiText()
    // Handled Strings
    class StringResource(@StringRes val resId : Int, vararg val args: Any): UiText()

    @Composable
    fun asString(): String {
        return when(this){
            is DynamicString -> value
            is StringResource -> {
                stringResource(resId, *args)
            }
        }
    }
}






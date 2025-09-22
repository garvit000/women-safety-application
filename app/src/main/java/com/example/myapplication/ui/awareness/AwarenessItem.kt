package com.example.myapplication.ui.awareness

data class AwarenessItem(
    val title: String,
    val content: String,
    val type: ItemType = ItemType.TIP // Example: TIP, ALERT, GUIDELINE
)

enum class ItemType {
    TIP,
    ALERT,
    GUIDELINE
}
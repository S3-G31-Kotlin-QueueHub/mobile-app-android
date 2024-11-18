package com.queue_hub.isis3510_s3_g31.ui.screens.recommended

enum class RecommendedCategory(val value: String) {
    ALL(value ="All"),
    LESSWAITINGTIMELASTHOUR(value="Less waiting time last hour")
}

fun getAllRecommendedCategory(): List<RecommendedCategory>{
    return listOf(
        RecommendedCategory.ALL,
        RecommendedCategory.LESSWAITINGTIMELASTHOUR
    )
}

fun getRecommendedCategory(value: String) : RecommendedCategory?{
    val map = RecommendedCategory.values().associateBy(RecommendedCategory::value)
    return map[value]
}
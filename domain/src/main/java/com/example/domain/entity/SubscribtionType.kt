package com.example.domain.entity

import kotlinx.serialization.Serializable

@Serializable
enum class SubscriptionType {
    REGULAR,
    PREMIUM
}
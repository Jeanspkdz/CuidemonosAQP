package com.jean.cuidemonosaqp.modules.auth.domain.model

enum class SyncStatus {
    SYNCING,
    SUCCESS,
    ERROR,
    NO_NETWORK // Cambiar OFFLINE por NO_NETWORK para coincidir con tu código
}
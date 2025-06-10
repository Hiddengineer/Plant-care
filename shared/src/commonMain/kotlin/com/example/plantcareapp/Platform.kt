package com.example.plantcareapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
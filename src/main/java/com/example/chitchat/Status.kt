package com.example.chitchat

data class Status(
    var userId: String? = null,
    var userName: String? = null,
    var text: String? = null
) {
    // Default (no-argument) constructor
    constructor() : this("", "", "")
}


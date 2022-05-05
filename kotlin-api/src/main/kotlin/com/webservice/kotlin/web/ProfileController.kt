package com.webservice.kotlin.web

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class ProfileController(
    private val env: Environment
) {
    @GetMapping("/profile")
    fun profile(): String {
        val profiles = env.activeProfiles
        val realProfiles = arrayOf("real", "real1", "real2")
        val defaultProfile = if (profiles.isEmpty()) "default" else profiles[0]

        return profiles.firstOrNull { o: String -> realProfiles.contains(o) } ?: defaultProfile
    }
}
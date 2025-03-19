package com.example.eventstreamer.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "http-endpoints")
data class HttpEndpointProperties(
    val endpoints: List<Endpoint> = emptyList()
) {
    data class Endpoint(
        val name: String,
        val url: String,
        val method: String,
        val headers: Map<String, String> = emptyMap(),
        val condition: String
    )
} 
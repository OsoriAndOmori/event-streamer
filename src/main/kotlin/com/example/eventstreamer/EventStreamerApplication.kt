package com.example.eventstreamer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventStreamerApplication

fun main(args: Array<String>) {
    runApplication<EventStreamerApplication>(*args)
} 
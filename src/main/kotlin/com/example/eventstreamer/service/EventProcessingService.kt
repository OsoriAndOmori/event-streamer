package com.example.eventstreamer.service

import com.example.eventstreamer.config.HttpEndpointProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class EventProcessingService(
    private val endpointProperties: HttpEndpointProperties,
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"])
    fun processEvent(message: String) {
        try {
            val event = objectMapper.readValue(message, Map::class.java) as Map<String, Any>
            
            endpointProperties.endpoints
                .filter { evaluateCondition(it.condition, event) }
                .forEach { sendHttpRequest(it, event) }
        } catch (e: Exception) {
            logger.error("Error processing message: $message", e)
        }
    }

    private fun evaluateCondition(condition: String, event: Map<String, Any>): Boolean {
        // TODO: Implement condition evaluation using SpEL or similar
        return true
    }

    private fun sendHttpRequest(endpoint: HttpEndpointProperties.Endpoint, event: Map<String, Any>) {
        try {
            val headers = HttpHeaders().apply {
                endpoint.headers.forEach { (key, value) -> set(key, value) }
            }

            val request = HttpEntity(event, headers)
            
            val response = restTemplate.exchange(
                endpoint.url,
                HttpMethod.valueOf(endpoint.method),
                request,
                String::class.java
            )

            logger.info("Successfully sent request to {}: {}", endpoint.name, response.statusCode)
        } catch (e: Exception) {
            logger.error("Error sending request to {}: {}", endpoint.name, e.message)
        }
    }
} 
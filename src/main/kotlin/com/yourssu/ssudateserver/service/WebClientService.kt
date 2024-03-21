package com.yourssu.ssudateserver.service

import com.yourssu.ssudateserver.exception.logic.WebClientException
import com.yourssu.ssudateserver.factory.logger
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException

@Service
class WebClientService(
    val kakaoDeleteProperties: KakaoDeleteProperties,
) {
    val log = logger()

    fun deleteUser(targetId: String): Boolean {
        println(targetId)
        val webClient =
            WebClient.builder()
                .baseUrl(kakaoDeleteProperties.removeUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK ${kakaoDeleteProperties.adminKey}")
                .build()

        try {
            val response =
                webClient.post()
                    .body(
                        BodyInserters.fromFormData("target_id_type", "user_id")
                            .with("target_id", targetId),
                    )
                    .retrieve()
                    .toEntity(String::class.java)
                    .block()

            if (response?.statusCode?.is2xxSuccessful == true) {
                return true
            } else {
                val errorMessage = response?.body ?: "Unknown error"
                log.error("WebClient error: $errorMessage")
                throw WebClientException("WebClient error: $errorMessage")
            }
        } catch (ex: WebClientResponseException) {
            log.error("WebClient error: ${ex.rawStatusCode} - ${ex.responseBodyAsString}")
            throw WebClientException("WebClient error : " + ex.message)
        } catch (ex: Exception) {
            log.error("Unexpected error: ${ex.message}")
            throw WebClientException("Unexpected error :" + ex.message)
        }
    }
}

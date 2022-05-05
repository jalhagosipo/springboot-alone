package com.webservice.kotlin.web

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestConstructor


@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerTest(
    private val restTemplate: TestRestTemplate
) {
    @Test
    @Throws(Exception::class)
    fun profile은_인증없이_호출된다() {
        val expected = "default"
        val response = restTemplate.getForEntity(
            "/profile",
            String::class.java
        )
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body).isEqualTo(expected)
    }
}
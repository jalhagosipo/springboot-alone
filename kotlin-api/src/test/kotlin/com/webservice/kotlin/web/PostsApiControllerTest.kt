package com.webservice.kotlin.web

import com.webservice.kotlin.domain.posts.Posts
import com.webservice.kotlin.domain.posts.PostsRepository
import com.webservice.kotlin.web.dto.PostsSaveRequestDto
import com.webservice.kotlin.web.dto.PostsUpdateRequestDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestConstructor

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostsApiControllerTest(
    private val restTemplate: TestRestTemplate,
    private val postsRepository: PostsRepository
) {
    @LocalServerPort
    private val port = 0

    @AfterEach
    fun tearDown() = postsRepository.deleteAll()

    @Test
    @DisplayName("Posts 등록된다")
    fun `Posts 등록된다FUN`() {
        //given
        val title = "title"
        val content = "content"

        val requestDto = PostsSaveRequestDto(title, content, "author")
        val url = "http://localhost:$port/api/v1/posts"

        // when
        val responseEntity: ResponseEntity<Long> = restTemplate.postForEntity(
            url, requestDto,
            Long::class.java
        )

        // then
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isGreaterThan(0L)

        val all = postsRepository.findAll()
        assertThat(all[0].title).isEqualTo(title)
        assertThat(all[0].content).isEqualTo(content)
    }

    @Test
    @DisplayName("Posts 수정된다")
    fun `Posts 수정된다FUN`() {
        //given
        val title = "title"
        val content = "content"
        val savedPosts = postsRepository.save(Posts(title, content, "jalhagosipo"))

        val updateId = savedPosts.id
        val expectedTitle = "title2"
        val expectedContent = "content2"
        val requestDto = PostsUpdateRequestDto(expectedTitle, expectedContent)

        val url = "http://localhost:$port/api/v1/posts/$updateId"

        val requestEntity: HttpEntity<PostsUpdateRequestDto> = HttpEntity(requestDto)

        //when
        val responseEntity = restTemplate
            .exchange(url, HttpMethod.PUT, requestEntity, Long::class.java)

        //then
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isGreaterThan(0L)

        val all = postsRepository.findAll()
        assertThat(all[0].title).isEqualTo(expectedTitle)
        assertThat(all[0].content).isEqualTo(expectedContent)
    }

}
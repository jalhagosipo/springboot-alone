package com.webservice.kotlin.domain.posts


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostsRepositoryTest(private val postsRepository: PostsRepository) {

    @AfterEach
    fun cleanUp() = postsRepository.deleteAll()

    @Test
    @DisplayName("게시글 저장 불러오기")
    fun `게시글 저장 불러오기FUN`() {
        //given
        val title = "테스트 게시글"
        val content = "테스트 본문"
        val author = "jalhagosipo@github.com"
        postsRepository.save(Posts(title = title, content = content, author = author))

        //when
        val posts = postsRepository.findAll()

        //then
        val post = posts[0]
        assertThat(post).isNotNull
        assertThat(post.title).isEqualTo(title)
        assertThat(post.content).isEqualTo(content)
    }

    @Test
    @DisplayName("BaseTimeEntity 등록")
    fun `BaseTimeEntity 등록FUN`() {
        //given
        val now = LocalDateTime.of(2019, 6, 4, 0, 0, 0)
        val title = "테스트 게시글"
        val content = "테스트 본문"
        val author = "jalhagosipo@github.com"
        postsRepository.save(Posts(title, content, author))

        //when
        val postsList = postsRepository.findAll()


        //then
        val post = postsList[0]

        println(">>>>>>>>> createDate= ${post.createdDate}, modifiedDate= ${post.modifiedDate}")

        assertThat(post.createdDate).isAfter(now)
        assertThat(post.modifiedDate).isAfter(now)
    }
}
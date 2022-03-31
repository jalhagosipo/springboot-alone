package com.webservice.kotlin.service.posts

import com.webservice.kotlin.domain.posts.PostsRepository
import com.webservice.kotlin.web.dto.PostsResponseDto
import com.webservice.kotlin.web.dto.PostsSaveRequestDto
import com.webservice.kotlin.web.dto.PostsUpdateRequestDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostsService (val postsRepository: PostsRepository) {

    @Transactional
    fun save(requestDto: PostsSaveRequestDto): Long? {
        return postsRepository.save(requestDto.toEntity()).id
    }

    @Transactional
    fun update(id: Long, requestDto: PostsUpdateRequestDto): Long {
        val posts = postsRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "해당 사용자가 없습니다. id=$id"
                )
            }
        posts.update(requestDto.title, requestDto.content)
        return id
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): PostsResponseDto {
        val entity = postsRepository.findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "해당 사용자가 없습니다. id=$id"
                )
            }
        return PostsResponseDto(entity)
    }
}
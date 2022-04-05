package com.webservice.kotlin.web.dto

import com.webservice.kotlin.domain.posts.Posts
import java.time.LocalDateTime


data class PostsListResponseDto(
    val id: Long,
    val title: String,
    val author: String,
    val modifiedDate: LocalDateTime
) {
    constructor(entity: Posts) : this(entity.id ?: 0, entity.title, entity.author, entity.modifiedDate
        ?: LocalDateTime.now())
}
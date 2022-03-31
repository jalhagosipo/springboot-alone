package com.webservice.kotlin.web.dto

import com.webservice.kotlin.domain.posts.Posts

data class PostsSaveRequestDto (
    val title: String,
    val content: String,
    val author: String
) {
    fun toEntity(): Posts {
        return Posts(title, content, author)
    }
}
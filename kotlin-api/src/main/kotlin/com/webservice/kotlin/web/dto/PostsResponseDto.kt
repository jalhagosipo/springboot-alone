package com.webservice.kotlin.web.dto

import com.webservice.kotlin.domain.posts.Posts


data class PostsResponseDto(val posts: Posts) {
    val id: Long? = posts.id
    val title: String = posts.title
    val content: String = posts.content
    val author: String = posts.author
}
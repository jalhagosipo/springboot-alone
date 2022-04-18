package com.webservice.kotlin.web

import com.webservice.kotlin.config.auth.LoginUser
import com.webservice.kotlin.config.auth.dto.SessionUser
import com.webservice.kotlin.service.posts.PostsService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable



@Controller
class IndexController(private val postsService: PostsService) {
    @GetMapping("/")
    fun index(model: Model, @LoginUser sessionUser: SessionUser?): String {
        model.addAttribute("posts", postsService.findAllDesc())
        sessionUser?.let {
            model.addAttribute("userName", sessionUser.name)
        }
        return "index"
    }

    @GetMapping("/posts/save")
    fun savePosts(): String = "posts-save"

    @GetMapping("/posts/update/{id}")
    fun postsUpdate(@PathVariable id: Long, model: Model): String {
        val dto = postsService.findById(id)
        model.addAttribute("post", dto)
        return "posts-update"
    }
}
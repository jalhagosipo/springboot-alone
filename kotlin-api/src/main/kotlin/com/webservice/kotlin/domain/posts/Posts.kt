package com.webservice.kotlin.domain.posts


import com.webservice.kotlin.domain.BaseTimeEntity
import javax.persistence.*

@Entity
class Posts(
    @Column(length = 500, nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    var author: String,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
): BaseTimeEntity()  {

    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }
}
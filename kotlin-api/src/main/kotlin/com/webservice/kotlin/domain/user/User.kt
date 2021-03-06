package com.webservice.kotlin.domain.user

import com.webservice.kotlin.domain.BaseTimeEntity
import javax.persistence.*


@Entity
class User(name: String, email: String, picture: String, role: Role) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    @Column(nullable = false)
    var name: String = name
        protected set
    @Column(nullable = false)
    var email: String = email
        protected set
    @Column
    var picture: String = picture
        protected set
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = role

    fun update(name: String, picture: String) : User {
        this.name = name
        this.picture = picture

        return this
    }

    fun getRoleKey(): String = this.role.key
}
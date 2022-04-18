package com.webservice.kotlin.config.auth.dto;


import com.webservice.kotlin.domain.user.User;

import java.io.Serializable;

data class SessionUser(val name: String, val email: String, val picture: String) : Serializable {
    constructor(user: User) : this(user.name, user.email, user.picture)
}
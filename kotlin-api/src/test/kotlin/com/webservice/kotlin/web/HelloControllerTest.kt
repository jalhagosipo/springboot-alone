package com.webservice.kotlin.web

import com.webservice.kotlin.config.auth.SecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap

@WebMvcTest(controllers = [HelloController::class],
    excludeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [SecurityConfig::class])
    ])
@ContextConfiguration(classes = [HelloController::class])
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WithMockUser(roles = ["USER"])
class HelloControllerTest(private val mockMvc: MockMvc) {

    @Test
    fun hello가_리턴된다() {
        val uri = "/hello"
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("hello"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun helloDto가_리턴된다() {
        val uri = "/hello/dto"
        val name = "hello"
        val amount = 1000

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("name", name)
        queryParams.add("amount", amount.toString())

        mockMvc.perform(MockMvcRequestBuilders.get(uri).queryParams(queryParams))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount))
    }
}

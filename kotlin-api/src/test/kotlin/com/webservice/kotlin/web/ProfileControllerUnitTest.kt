package com.webservice.kotlin.web

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.mock.env.MockEnvironment


class ProfileControllerUnitTest {

    @Test
    fun real_profile이_조회된다() {
        //given
        val expectedProfile = "real"
        val env = MockEnvironment()
        env.addActiveProfile(expectedProfile)
        env.addActiveProfile("oauth")
        env.addActiveProfile("real-db")

        val controller = ProfileController(env)

        //when
        val profile: String = controller.profile()

        //then
        Assertions.assertThat(profile).isEqualTo(expectedProfile)
    }

    @Test
    fun real_profile이_없으면_첫번째가_조회된다() {
        //given
        val expectedProfile = "oauth"
        val env = MockEnvironment()
        env.addActiveProfile(expectedProfile)
        env.addActiveProfile("real-db")

        val controller = ProfileController(env)

        //when
        val profile: String = controller.profile()

        //then
        Assertions.assertThat(profile).isEqualTo(expectedProfile)
    }

    @Test
    fun active_profile이_없으면_default가_조회된다() {
        //given
        val expectedProfile = "default"
        val env = MockEnvironment()

        val controller = ProfileController(env)

        //when
        val profile: String = controller.profile()

        //then
        Assertions.assertThat(profile).isEqualTo(expectedProfile)
    }
}

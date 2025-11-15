package org.megamind.mycashpoint_api.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRespository : JpaRepository<User, Long> {

    fun findByUserName(username: String): User?
}
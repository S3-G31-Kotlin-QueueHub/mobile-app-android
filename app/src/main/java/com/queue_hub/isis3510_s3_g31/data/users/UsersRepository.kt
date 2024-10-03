package com.queue_hub.isis3510_s3_g31.data.users

import com.queue_hub.isis3510_s3_g31.data.users.remote.LoginRequest
import com.queue_hub.isis3510_s3_g31.data.users.remote.UserApi


class UsersRepository(
    private val apiUsers : UserApi
) {

    suspend fun authUser(email: String, password: String): Boolean{
        try {
            val response = apiUsers.login(LoginRequest(email, password))
            return true
        }catch (e: Exception){
            e.printStackTrace()
            return false
        }
    }
}
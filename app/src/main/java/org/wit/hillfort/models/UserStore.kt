package org.wit.hillfort.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun findOne(user: UserModel): UserModel
    fun create(user: UserModel): UserModel
    fun delete(user: UserModel)
    fun authenticate(user: UserModel): UserModel
    fun update(user: UserModel)
    fun isUsernameRegistered(username: String): Boolean
}

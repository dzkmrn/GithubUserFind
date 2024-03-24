package com.dicoding.githubusersearch.data.remote.response

import com.google.gson.annotations.SerializedName

data class ConnectionResponse(

	@field:SerializedName("ConnectionResponse")
	val connectionResponse: List<ConnectionResponseItem?>? = null
)

data class ConnectionResponseItem(

	@field:SerializedName("following_url")
	val followingUrl: String? = null,

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("followers_url")
	val followersUrl: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

)

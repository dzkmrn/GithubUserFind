package com.dicoding.githubusersearch.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubusersearch.data.remote.response.GithubResponse
import com.dicoding.githubusersearch.data.remote.response.ItemsItem
import com.dicoding.githubusersearch.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _githubresponse = MutableLiveData<GithubResponse>()
    val githubResponse : LiveData<GithubResponse> = _githubresponse

    private val _listProfile = MutableLiveData<List<ItemsItem>>()
    val listProfile : LiveData<List<ItemsItem>> = _listProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        searchUser("Aldi")
    }

    fun searchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listProfile.value = response.body()?.items as List<ItemsItem>
                } else {
                    Log.e("Fail", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("APIRequest", "onFailure: ${t.message}")

                t.printStackTrace()
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}
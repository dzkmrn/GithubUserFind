package com.dicoding.githubusersearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubusersearch.R
import com.dicoding.githubusersearch.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_connect_1,
            R.string.tab_connect_2
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username") ?: ""

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        viewModel.isLoading.observe(this) { isLoading ->
            if (!isLoading) {
                setupViewPager(username)
            }
            showLoading(isLoading)
        }

        if (!username.isNullOrEmpty()) {
            viewModel.getUserDetail(username)
        }

        viewModel.detailUserResponse.observe(this) { userDetail ->
            if (userDetail != null) {
                binding.profileName.text = userDetail.name ?: "Unknown"
                binding.profileAccountName.text = userDetail.login ?: "Unknown"
                binding.tvFollowers.text = "${userDetail.followers ?: 0} Followers"
                binding.tvFollowing.text = "${userDetail.following ?: 0} Following"
                Glide.with(this)
                    .load(userDetail.avatarUrl)
                    .placeholder(R.drawable.logo_app)
                    .error(R.drawable.logo_app)
                    .into(binding.imgItemPhoto)
            } else {
                Log.d("UserDetail", "Null Detail")
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupViewPager(username: String) {
        val sectionPagerAdapter = SectionPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
package com.dicoding.githubusersearch.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusersearch.R
import com.dicoding.githubusersearch.data.remote.response.ItemsItem
import com.dicoding.githubusersearch.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val profileAdapter = ProfileAdapter(showBookmark = true)
    private lateinit var viewModel: MainViewModel
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var bookmarkViewModelFactory: BookmarkViewModelFactory
    private lateinit var bookmarkViewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1_500)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)
        val imageViewDarkMode = findViewById<ImageView>(R.id.switch_theme_icon)

        val pref = SettingPreferences.getInstance(application.dataStore)

        viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(MainViewModel::class.java)

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        bookmarkViewModelFactory = BookmarkViewModelFactory.getInstance(this)
        bookmarkViewModel = ViewModelProvider(this, bookmarkViewModelFactory).get(BookmarkViewModel::class.java)

        mainViewModel.listProfile.observe(this) { consumerProfile ->
            setProfileData(consumerProfile, bookmarkViewModel)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvProfile.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvProfile.addItemDecoration(itemDecoration)

        binding.rvProfile.adapter = profileAdapter

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                false
            }
            searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val query = s.toString().trim()
                    showLoading(true)
                    viewModel.searchUser(query)
                }
            })
            searchView.setText(searchView.text)
        }

        binding.fabBookmark.setOnClickListener {
            val intent = Intent(this@MainActivity, BookmarkActivity::class.java)
            startActivity(intent)
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                imageViewDarkMode.setImageResource(R.drawable.baseline_dark_mode_24_white)
            } else {
                imageViewDarkMode.setImageResource(R.drawable.baseline_dark_mode_24)
            }
            mainViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setProfileData(consumerProfiles: List<ItemsItem>, bookmarkViewModel: BookmarkViewModel) {
        val adapter = ProfileAdapter(bookmarkViewModel)
        adapter.submitList(consumerProfiles)
        binding.rvProfile.adapter = adapter
    }

    private fun showLoading(state: Boolean) { if (state) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE }
}

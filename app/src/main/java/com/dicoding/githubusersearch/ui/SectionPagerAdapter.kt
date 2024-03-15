package com.dicoding.githubusersearch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ConnectionFragment()
        fragment.arguments = Bundle().apply {
            putInt(ConnectionFragment.ARG_SECTION_NUMBER, position + 1)
            putString(ConnectionFragment.ARG_USERNAME, username)
        }
        return fragment
    }
}
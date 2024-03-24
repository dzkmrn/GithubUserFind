package com.dicoding.githubusersearch.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubusersearch.data.remote.response.ItemsItem
import com.dicoding.githubusersearch.databinding.FragmentConnectionBinding
import com.dicoding.githubusersearch.databinding.ItemProfileBinding


class ConnectionFragment : Fragment() {

    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ConnectionViewModel
    private lateinit var followersAdapter: ProfileAdapter
    private lateinit var followingAdapter: ProfileAdapter
    private var itemProfileBinding: ItemProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConnectionBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersAdapter = ProfileAdapter(showBookmark = false)
        binding?.rvFollowers?.adapter = followersAdapter

        followingAdapter = ProfileAdapter(showBookmark = false)
        binding?.rvFollowings?.adapter = followingAdapter

        arguments?.let {
            val position = it.getInt(ARG_SECTION_NUMBER)
            val username = it.getString(ARG_USERNAME)

            viewModel = ViewModelProvider(this).get(ConnectionViewModel::class.java)

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            if (position == 2) {
                viewModel.getFollowing(username ?: "")
                viewModel.listFollowing.observe(viewLifecycleOwner) { followingList ->
                    Log.d("FollowingList", "Following List: $followingList")
                    followingAdapter.submitList(followingList as List<ItemsItem>)
                    Log.d("FollowingListSize", "Following List Size: ${followingList?.size ?: 0}")
                }
            } else {
                viewModel.getFollowers(username ?: "")
                viewModel.listFollowers.observe(viewLifecycleOwner) { followersList ->
                    Log.d("FollowersList", "Followers List: $followersList")
                    followersAdapter.submitList(followersList  as List<ItemsItem>)
                    Log.d("FollowersListSize", "Followers List Size: ${followersList?.size ?: 0}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}

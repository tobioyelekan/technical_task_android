package com.example.userapp.ui.users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.userapp.R
import com.example.userapp.data.helper.Resource
import com.example.userapp.databinding.ActivityUsersBinding
import com.example.userapp.model.User
import com.example.userapp.ui.adduser.AddUserDialogFragment
import com.example.userapp.utils.show
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersActivity : AppCompatActivity(), AddUserDialogFragment.AddUserListener {

    private val viewModel by viewModels<UsersViewModel>()
    private var progressDialog: AlertDialog? = null

    private lateinit var binding: ActivityUsersBinding
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()

        binding.addUser.setOnClickListener {
            AddUserDialogFragment().show(supportFragmentManager, "dialog")
        }
    }

    private fun setupRecyclerView() {
        adapter = UserListAdapter {
            showShouldRemoveUserDialog(it.id)
        }
        binding.userList.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.users.observe(this, {
            adapter.submitList(it)
        })

        viewModel.loadingFetchUser.observe(this, {
            when (it) {
                is Resource.Loading -> binding.progress.isVisible = true
                is Resource.Success -> binding.progress.isVisible = false
                is Resource.Error -> {
                    binding.progress.isVisible = false
                    show(it.message)
                }
            }
        })

        viewModel.loading.observe(this, {
            when (it) {
                is Resource.Loading -> showProgressAlertDialog()
                is Resource.Success -> hideProgressDialog()
                is Resource.Error -> {
                    hideProgressDialog()
                    show(it.message)
                }
            }
        })
    }

    private fun hideProgressDialog() {
        progressDialog?.let { alertDialog ->
            if (alertDialog.isShowing) alertDialog.dismiss()
        }
    }

    private fun showShouldRemoveUserDialog(id:String) {
        MaterialAlertDialogBuilder(this)
            .setPositiveButton(getString(R.string.dialog_yes)) { dialog, _ ->
                dialog.dismiss()
                viewModel.removeUser(id)
            }
            .setNegativeButton(R.string.dialog_no) { dialog, _ -> dialog.dismiss() }
            .setMessage(R.string.ask_delete_user)
            .show()
    }

    private fun showProgressAlertDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null)
        progressDialog = MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setView(view)
            .show()
    }

    override fun proceed(user: User) {
        viewModel.addUser(user)
    }
}
package com.example.userapp.ui.adduser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.userapp.databinding.DialogAddUserBinding
import com.example.userapp.model.User
import com.example.userapp.utils.EventObserver
import com.example.userapp.utils.hideKeyboard
import com.example.userapp.utils.show

class AddUserDialogFragment : DialogFragment() {

    private var _binding: DialogAddUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddUserViewModel>()

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            addUser.setOnClickListener {
                val name = nameField.text.toString()
                val email = emailField.text.toString()
                val selectedRadioId = genderGroup.checkedRadioButtonId

                val gender = when (val checkedButton =
                    genderGroup.findViewById<RadioButton>(selectedRadioId)) {
                    null -> ""
                    else -> checkedButton.text.toString().lowercase()
                }

                viewModel.validate(name = name, email = email, gender = gender)
            }

            cancel.setOnClickListener { dismiss() }
        }
    }

    private fun setupObservers() {
        viewModel.validateInput.observe(viewLifecycleOwner, {
            context?.show(it)
        })

        viewModel.proceed.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard(this)
            (requireActivity() as AddUserListener).proceed(it)
            dismiss()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface AddUserListener {
        fun proceed(user: User)
    }

}
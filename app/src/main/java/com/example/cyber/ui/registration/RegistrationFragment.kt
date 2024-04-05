package com.example.cyber.ui.registration

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cyber.R
import com.example.cyber.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isRegistered = sharedPreferences.getBoolean("isRegistered", false)


        if (isRegistered) {
            binding.editTextUsername.visibility = View.VISIBLE
            binding.editTextPassword.visibility = View.VISIBLE
            binding.buttonRegister.text = "Войти"

            binding.buttonRegister.setOnClickListener {
                val enteredUsername = binding.editTextUsername.text.toString()
                val enteredPassword = binding.editTextPassword.text.toString()

                val savedUsername = sharedPreferences.getString("username", "")
                val savedPassword = sharedPreferences.getString("password", "")

                if (enteredUsername == savedUsername && enteredPassword == savedPassword) {
                    Toast.makeText(requireContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_profile)
                } else {
                    Toast.makeText(requireContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.editTextFirstName.visibility = View.VISIBLE
            binding.editTextLastName.visibility = View.VISIBLE
            binding.editTextDOB.visibility = View.VISIBLE
            binding.editTextEmail.visibility = View.VISIBLE
            binding.editTextPassword.visibility = View.VISIBLE
            binding.editTextConfirmPassword.visibility = View.VISIBLE
            binding.editTextUsername.visibility = View.VISIBLE

            binding.buttonRegister.setOnClickListener {
                val firstName = binding.editTextFirstName.text.toString()
                val lastName = binding.editTextLastName.text.toString()
                val dob = binding.editTextDOB.text.toString()
                val email = binding.editTextEmail.text.toString()
                val username = binding.editTextUsername.text.toString()
                val password = binding.editTextPassword.text.toString()
                val confirmPassword = binding.editTextConfirmPassword.text.toString()

                if (validateRegistrationData(firstName, lastName, dob, email, username, password, confirmPassword)) {
                    saveUserData(firstName, lastName, dob, email, username, password)
                    findNavController().navigate(R.id.navigation_profile)
                    Toast.makeText(requireContext(), "Регистрация успешно завершена", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateRegistrationData(firstName: String, lastName: String, dob: String, email: String, username: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (firstName.isEmpty()) {
            binding.editTextFirstName.error = "Введите имя"
            isValid = false
        }

        if (lastName.isEmpty()) {
            binding.editTextLastName.error = "Введите фамилию"
            isValid = false
        }

        if (dob.isEmpty()) {
            binding.editTextDOB.error = "Введите дату рождения"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.editTextEmail.error = "Введите email"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmail.error = "Введите корректный email"
            isValid = false
        }

        if (username.isEmpty()) {
            binding.editTextUsername.error = "Введите логин"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "Введите пароль"
            isValid = false
        } else if (password.length < 6) {
            binding.editTextPassword.error = "Пароль должен содержать не менее 6 символов"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "Подтвердите пароль"
            isValid = false
        } else if (confirmPassword != password) {
            binding.editTextConfirmPassword.error = "Пароли не совпадают"
            isValid = false
        }

        return isValid
    }

    private fun saveUserData(firstName: String, lastName: String, dob: String, email: String, username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("firstName", firstName)
        editor.putString("lastName", lastName)
        editor.putString("dob", dob)
        editor.putString("email", email)
        editor.putString("username", username)
        editor.putString("password", password)
        editor.putBoolean("isLoggedIn", true)
        editor.putBoolean("isRegistered", true)
        editor.apply()
    }
}

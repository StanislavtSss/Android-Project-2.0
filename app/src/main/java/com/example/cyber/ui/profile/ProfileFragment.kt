package com.example.cyber.ui.profile

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cyber.R
import com.example.cyber.databinding.FragmentProfileBinding
import java.util.Calendar

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editor = sharedPreferences.edit()
        binding.textViewFirstName.text = "${sharedPreferences.getString("firstName", "")}"
        binding.textViewLastName.text = "${sharedPreferences.getString("lastName", "")}"
        binding.textViewDOB.text = "${sharedPreferences.getString("dob", "")}"
        binding.textViewEmail.text = "${sharedPreferences.getString("email", "")}"
        binding.textViewUsername.text = "${sharedPreferences.getString("username", "")}"

        binding.textViewFirstName.setOnClickListener { showPopupForTextView(binding.textViewFirstName, "firstName") }
        binding.textViewLastName.setOnClickListener { showPopupForTextView(binding.textViewLastName, "lastName") }



        //binding.textViewDOB.setOnClickListener { showPopupForTextView(binding.textViewDOB, "dob") }
        binding.textViewDOB.setOnClickListener {
            val currentDate = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"

                    saveSelectedDate(selectedDate)

                    binding.textViewDOB.text = selectedDate
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }



        binding.textViewEmail.setOnClickListener { showPopupForTextView(binding.textViewEmail, "email") }
        binding.textViewUsername.setOnClickListener { showPopupForTextView(binding.textViewUsername, "username") }
        binding.logoutButton.setOnClickListener {
            editor.putBoolean("isLoggedIn", false)
            findNavController().navigate(R.id.navigation_registration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showPopupForTextView(textView: TextView, key: String) {
        if (!isAdded) {
            return
        }

        val inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_edit_text, null)
        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAtLocation(textView, Gravity.CENTER, 0, 0)

        val editText = popupView.findViewById<EditText>(R.id.editTextValue)
        editText.setText(textView.text.toString())

        popupView.findViewById<View>(R.id.buttonSave).setOnClickListener {
            val newValue = editText.text.toString()
            textView.text = newValue
            sharedPreferences.edit().putString(key, newValue).apply()
            popupWindow.dismiss()
            Toast.makeText(requireContext(), "Значение обновлено", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveSelectedDate(selectedDate: String) {
        val editor = sharedPreferences.edit()
        sharedPreferences.edit().putString("dob", selectedDate).apply()
        editor.apply()
    }


}

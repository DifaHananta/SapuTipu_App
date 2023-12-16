package com.project.saputipuapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.project.saputipuapp.R
import com.project.saputipuapp.data.local.User
import com.project.saputipuapp.data.local.UserPreference
import com.project.saputipuapp.databinding.ActivityLoginBinding
import com.project.saputipuapp.ui.MainActivity
import com.project.saputipuapp.ui.ViewModelFactory
import com.project.saputipuapp.ui.register.RegisterActivity
import com.project.saputipuapp.utils.ApiCallbackString

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]

        setButtonRegisterActivity()
        getUserData()
        setButtonLogin()
    }

    private fun getUserData() {
        viewModel.getUser().observe(this) {
            user = User(
                it.token
            )
        }
    }

    private fun setButtonLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            viewModel.login(email, password, object : ApiCallbackString {
                override fun onResponse(success: Boolean, message: String) {
                    val filterMessage = message.replace(",", " ")
                    val cleanedMessage = filterMessage.filter { it.isLetterOrDigit() || it.isWhitespace() }
                    if (success) {
                        Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, getString(R.string.login_failed) + " " + cleanedMessage, Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
    }

    private fun setButtonRegisterActivity() {
        binding.btnRegisterActivity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    }
}
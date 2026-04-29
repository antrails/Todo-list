package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var buttonRegister: Button
    private lateinit var buttonLogin: Button
    private lateinit var textViewForgotPassword: TextView
    private lateinit var editTextPassword: EditText
    private lateinit var editTextAccount: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextAccount = findViewById<EditText>(R.id.editText_account)
        editTextPassword = findViewById<EditText>(R.id.editText_passwd)
        buttonLogin = findViewById<Button>(R.id.button_Login)
        buttonRegister = findViewById<Button>(R.id.button_Register)
        val userDao = DatabaseProvider.getDatabase(this).userDao()

        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val account = editTextAccount.text.toString()
            val password = editTextPassword.text.toString()

            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO){
                    userDao.login(account,password)
                }
                if (user != null){
                    SessionManager.currentUser = user
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Toast.makeText(this@LoginActivity, "登入失敗,帳號不存在或密碼錯誤", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}
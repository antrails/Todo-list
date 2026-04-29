package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var buttonCancel: Button
    private lateinit var buttonConfirmRegister: Button
    private lateinit var editTextRegisterSecurityQus: EditText
    private lateinit var editTextRegisterConfirmPassword: EditText
    private lateinit var editTextRegisterPassword: EditText
    private lateinit var editTextRegisterAccount: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextRegisterAccount = findViewById<EditText>(R.id.editText_register_account)
        editTextRegisterPassword = findViewById<EditText>(R.id.editText_register_password)
        editTextRegisterConfirmPassword = findViewById<EditText>(R.id.editText_register_confirm_password)
        editTextRegisterSecurityQus = findViewById<EditText>(R.id.editText_securityQuestion)
        buttonConfirmRegister = findViewById<Button>(R.id.button_register_confirm)
        buttonCancel = findViewById<Button>(R.id.button_register_cancel)
        val userDao = DatabaseProvider.getDatabase(this).userDao()

        buttonCancel.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val user = User(
            account = editTextRegisterAccount.text.toString(),
            password = editTextRegisterPassword.text.toString(),
            securityAnswer = editTextRegisterSecurityQus.text.toString()
        )

        buttonConfirmRegister.setOnClickListener {

            val account = editTextRegisterAccount.text.toString()
            val password = editTextRegisterPassword.text.toString()
            val confirmPassword = editTextRegisterConfirmPassword.text.toString()
            val security = editTextRegisterSecurityQus.text.toString()

            if (account.isNotEmpty() && password.isNotEmpty() &&
                confirmPassword.isNotEmpty() && security.isNotEmpty()
            ) {

                if (password == confirmPassword) {

                    val user = User(
                        account = account,
                        password = password,
                        securityAnswer = security
                    )

                    lifecycleScope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                userDao.insertUser(user)
                                FirebaseUserRepository.uploadUser(user)
                            }

                            Toast.makeText(this@RegisterActivity, "註冊成功，請登入", Toast.LENGTH_SHORT).show()
                            finish()

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(this@RegisterActivity, "註冊失敗", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "密碼不一致", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "請填寫所有欄位", Toast.LENGTH_SHORT).show()
            }
        }



    }
}
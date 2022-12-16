
package com.nomeapp.activities



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.nomeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.nomeapp.models.FirebaseAuthWrapper
import com.nomeapp.models.FirebaseDbWrapper
import com.nomeapp.models.User
import com.nomeapp.models.usernameAlreadyExists
import kotlinx.coroutines.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class RegisterActivity : AppCompatActivity() {
    val context: Context = this
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val switch: TextView = findViewById<View>(R.id.switchToLogin) as TextView
        val LoginButton: Button = findViewById<View>(R.id.registerButton) as Button

        switch.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        LoginButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val Name: EditText = findViewById<View>(R.id.Name) as EditText
                val Surname: EditText = findViewById<View>(R.id.Surname) as EditText
                val userName: EditText = findViewById<View>(R.id.userName) as EditText
                val email: EditText = findViewById<View>(R.id.userEmail) as EditText
                val password: EditText = findViewById<View>(R.id.userPassword) as EditText

                if (Name.text.isEmpty() || Surname.text.isEmpty() || userName.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty()) {
                    Name.setError("This is required")
                    Surname.setError("This is required")
                    userName.setError("This is required")
                    email.setError("This is required")
                    password.setError("This is required")
                    return
                }

                if (usernameAlreadyExists(context, userName.text.toString())) {
                    Log.e("passaggio", usernameAlreadyExists(context, userName.text.toString()).toString())
                    Toast.makeText(context, "Username already in use.", Toast.LENGTH_SHORT).show()
                    return
                }

                val user = User(
                    userName.text.toString(),
                    Name.text.toString(),
                    Surname.text.toString(),
                    "null"
                )

                action(user, email.text.toString(), password.text.toString())

            }

        })
    }

    fun action(user: User, email: String, password: String) {
        val firebaseAuthWrapper: FirebaseAuthWrapper = FirebaseAuthWrapper(context)
        firebaseAuthWrapper.signUp(user, email, password)
        //email already used
    }
}
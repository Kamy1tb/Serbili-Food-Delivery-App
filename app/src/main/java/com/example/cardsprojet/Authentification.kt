package com.example.cardsprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cardsprojet.DAO.ApiClient
import com.example.cardsprojet.databinding.ActivityAuthentificationBinding
import com.example.cardsprojet.databinding.ActivityMainBinding
import com.example.cardsprojet.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject



class Authentification : AppCompatActivity() {
    lateinit var binding: ActivityAuthentificationBinding
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(AuthentificationFragment())
        /*setContentView(R.layout.activity_authentification)
        val SignInButton = findViewById<Button>(R.id.button2)

        SignInButton.setOnClickListener{
            val context = this@Authentification
            CoroutineScope(Dispatchers.Main).launch {
                val username = findViewById<EditText>(R.id.editTextTextEmailAddress)
                val password = findViewById<EditText>(R.id.editTextTextPassword)
                val response = verifySignIn(username.text.toString(), password.text.toString()).await()
                val data = response.toString()
                val message = data
                val tag = "MineTaghh"
                Log.d(tag, message)
                if (message == "Incorrect username or password"){
                    Toast.makeText(context,message,Toast.LENGTH_LONG).show()
                }
                else{
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("user", data)
                    startActivity(intent)
                }

            }
        }*/
    }


    private fun replaceFragment(homeFragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_authentif,homeFragment)
        fragmentTransaction.commit()
    }


    fun createPartFromString(username: String,password: String): MultipartBody {
        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()
    }



}


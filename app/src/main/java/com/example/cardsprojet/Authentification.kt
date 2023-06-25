package com.example.cardsprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cardsprojet.DAO.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject



class Authentification : AppCompatActivity() {
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)
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
        }
    }



    private suspend fun verifySignIn(username:String,password: String) =
        CoroutineScope(Dispatchers.IO).async {
            val jsonData = mapOf(
                "username" to username,
                "password" to password
            )
            try{
                val response = ApiClient.apiService.signIn(jsonData)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorJson = JSONObject(errorResponse)
                    val errorMessage = errorJson.getString("detail")
                    errorMessage
                }

            } catch (e : Exception){
                e.printStackTrace()
            }


        }

    fun createPartFromString(username: String,password: String): MultipartBody {
        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()
    }



}


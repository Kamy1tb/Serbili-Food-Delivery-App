package com.example.cardsprojet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class Authentification : AppCompatActivity() {
    //@SuppressLint("MissingInflatedId")
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)
        val SignInButton = findViewById<Button>(R.id.button2)

        SignInButton.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                val username = findViewById<EditText>(R.id.editTextTextEmailAddress)
                val password = findViewById<EditText>(R.id.editTextTextPassword)
                val response = verifySignIn(username.text.toString(),password.text.toString()).await()
                withContext(Dispatchers.Main) {
// code de la réponse 200
                        val data = response.toString()
                        username.setText(data)

                }
            }
        }
    }

    private fun verifySignIn(username:String,password: String) =
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


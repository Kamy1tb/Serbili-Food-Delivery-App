package com.example.cardsprojet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cardsprojet.DAO.ApiClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class SignUp : Fragment() {
    private lateinit var myView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        return myView
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val SignUpButton = myView.findViewById<Button>(R.id.confirm)
        SignUpButton.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                val username = myView.findViewById<EditText>(R.id.usernamesignup)
                val lastname = myView.findViewById<EditText>(R.id.lastname)
                val firstname = myView.findViewById<EditText>(R.id.firstname)
                val profil_pic = "oumbaed"
                val phone = myView.findViewById<EditText>(R.id.phoneSignUp)
                val adress = myView.findViewById<EditText>(R.id.adresssignup)
                val email = myView.findViewById<EditText>(R.id.emailSignup)
                val password = myView.findViewById<EditText>(R.id.passwordsignup)
                val response = verifySignUp(username.text.toString(),password.text.toString(),email.text.toString(),firstname.text.toString(),lastname.text.toString(),phone.text.toString(),adress.text.toString(),profil_pic).await()
                withContext(Dispatchers.Main) {
// code de la réponse 200
                    val data = response
                    val message = data.toString()

                    if (message == "Invalid Signup, Try again"){
                        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
                    }
                    else{
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        val gson = GsonBuilder().serializeNulls().create()
                        val jsonString = gson.toJson(data)
                        intent.putExtra("user", jsonString)
                        //Toast.makeText(context,data.toString(), Toast.LENGTH_LONG).show()
                        startActivity(intent)
                        requireActivity().finish()
                    }

                }
            }
        }
    }

    private suspend fun verifySignUp(username:String,password: String,email:String,firstname:String,lastname:String,phone:String,address:String,profil_pic: String) =
        CoroutineScope(Dispatchers.IO).async {
            val jsonData = mapOf(
                "username" to username,
                "password" to password,
                "email" to email,
                "firstname" to firstname,
                "lastname" to lastname,
                "phone" to phone,
                "address" to address,
                "profile_pic" to profil_pic
            )
            try{
                val response = ApiClient.apiService.signUp(jsonData)
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
}
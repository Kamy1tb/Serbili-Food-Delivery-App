package com.example.cardsprojet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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


class AuthentificationFragment : Fragment() {
    private lateinit var myView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val SignInButton = myView.findViewById<Button>(R.id.button2)
        val SignUpButton = myView.findViewById<TextView>(R.id.SignUp)
        SignUpButton.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frame_authentif, SignUp())
            transaction.commit()

        }
        SignInButton.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                val username = myView.findViewById<EditText>(R.id.editTextTextEmailAddress)
                val password = myView.findViewById<EditText>(R.id.editTextTextPassword)
                val response = verifySignIn(username.text.toString(),password.text.toString()).await()
                withContext(Dispatchers.Main) {
// code de la réponse 200
                    val data = response
                    val message = data.toString()

                    if (message == "Incorrect username or password"){
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_authentification, container, false)
        return myView
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
}
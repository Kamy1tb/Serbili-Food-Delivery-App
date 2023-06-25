package com.example.cardsprojet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.cardsprojet.DAO.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthentificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthentificationFragment : Fragment() {
    private lateinit var myView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val SignInButton = myView.findViewById<Button>(R.id.button2)
        SignInButton.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                val username = myView.findViewById<EditText>(R.id.editTextTextEmailAddress)
                val password = myView.findViewById<EditText>(R.id.editTextTextPassword)
                val response = verifySignIn(username.text.toString(),password.text.toString()).await()
                withContext(Dispatchers.Main) {
// code de la réponse 200
                    val data = response.toString()
                    username.setText(data)

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

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthentificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
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
}
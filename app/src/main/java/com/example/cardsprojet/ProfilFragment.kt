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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cardsprojet.DAO.AppDatabase
import com.example.cardsprojet.models.UserIT
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfilFragment : Fragment() {

    private lateinit var myView: View
    lateinit var database: AppDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_profil, container, false)
        return myView
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val logoutbutton: Button = view.findViewById(R.id.logout)
        getUser()

        //Toast.makeText(requireContext(),person.get("address").asString,Toast.LENGTH_LONG).show()

        logoutbutton.setOnClickListener{
            logout()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private  fun getUser() {
        GlobalScope.launch(Dispatchers.IO) {

                val user =database.userDao().getUser()
                val iamge = myView.findViewById<ImageView>(R.id.profil_pic)
                val username = myView.findViewById<TextView>(R.id.username_text)
                username.text = user.username
            activity?.runOnUiThread {
                Glide.with(requireContext()).load(user.image).apply(RequestOptions.circleCropTransform()).into(iamge)
            }


            // Faites quelque chose avec l'utilisateur récupéré ici
        }
    }

    private fun logout(){
        GlobalScope.launch(Dispatchers.IO) {
            database.userDao().deleteUser()
        }
    }
}
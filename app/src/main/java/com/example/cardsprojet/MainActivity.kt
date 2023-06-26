package com.example.cardsprojet

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.example.cardsprojet.DAO.AppDatabase

import com.example.cardsprojet.databinding.ActivityMainBinding
import com.example.cardsprojet.models.UserIT
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var database: AppDatabase;


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Restos())
        var user = intent.getStringExtra("user")
        if (user != null) {
            Log.d("usertest",user)
            val person = Gson().fromJson(user, JsonObject::class.java)

           Toast.makeText(this,"Bievenu ! "+person.get("first_name").asString,Toast.LENGTH_LONG).show()
            insertUserCach(person.get("id_user").asInt,person.get("first_name").asString,person.get("last_name").asString,person.get("username").asString,person.get("address").asString,person.get("mail").asString,person.get("phone").asString,person.get("profile_pic").asString)

        }
        else {
            Log.d("usertest","disconnected")
        }
        binding.bottomNavigationView.setOnItemSelectedListener {

        when(it.itemId){
            R.id.restaurants-> replaceFragment(Restos())
            R.id.commands-> {
                CoroutineScope(Dispatchers.Main).launch {
                    //Toast.makeText(this@MainActivity,verifyCache().await().toString(),Toast.LENGTH_LONG).show()
                    if (verifyCache().await().toInt() == 0) {
                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@MainActivity, Authentification::class.java)
                            startActivity(intent)
                        }

                    } else {

                        val fragment = CommandeFragment()
                        val bundle = Bundle()

                        replaceFragment(fragment)
                    }
                }
            }
            R.id.profil -> {
                CoroutineScope(Dispatchers.Main).launch {
                    //Toast.makeText(this@MainActivity,verifyCache().await().toString(),Toast.LENGTH_LONG).show()
                    if (verifyCache().await().toInt() == 0) {
                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@MainActivity, Authentification::class.java)
                            startActivity(intent)
                        }

                    } else {

                        val fragment = ProfilFragment()
                        val bundle = Bundle()
                        bundle.putString(
                            "user",
                            user
                        ) // Remplacez "key" par la clé souhaitée et "value" par la valeur à transmettre
                        fragment.arguments = bundle
                        replaceFragment(fragment)
                    }
                }

            }

            else-> {

            }
        }

            true

        }




    }

    private fun replaceFragment(homeFragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_resto,homeFragment)
        fragmentTransaction.commit()
    }

    private fun insertUserCach(id_user:Int,firstname:String,lastname:String,username:String,adress:String,mail:String,phone:String,image:String){
        GlobalScope.launch(Dispatchers.IO) {

            val usercache = UserIT(id_user,firstname,lastname,adress,username,image,phone,mail)
            val count =  database.userDao().insertUser(usercache)

            }

        }



    private suspend fun verifyCache()=
        CoroutineScope(Dispatchers.IO).async {
            database.userDao().verifyLogged()
    }
    }


package com.example.cardsprojet

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment

import com.example.cardsprojet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Restos())

        binding.bottomNavigationView.setOnItemSelectedListener {

        when(it.itemId){
            R.id.restaurants-> replaceFragment(Restos())
            R.id.profil -> {val intent = Intent(this, Authentification::class.java)
            startActivity(intent)
            }

            else-> {

            }
        }

            true

        }


        /*val intent = Intent(this, Authentification::class.java)
        startActivity(intent)*/
         /*binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //binding.recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            val restaurants = loadData().await()
            binding.recyclerView.adapter = MyAdapter(restaurants)
        }*/



    }

    private fun replaceFragment(homeFragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_resto,homeFragment)
        fragmentTransaction.commit()
    }

}
package com.example.cardsprojet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cardsprojet.databinding.ActivityMainBinding
import com.example.cardsprojet.databinding.ActivityMenuBinding


class Menu : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val restaurantId = intent.getIntExtra("restaurantId", 0)
        val val_type = intent.getStringExtra("type_resto")
        val val_nom = intent.getStringExtra("nom_resto")
        val val_rating = intent.getIntExtra("rating_resto",0)
        val val_image = intent.getStringExtra("image_resto")
        val Cuisine_type = findViewById<TextView>(R.id.cuis_type)
        val nom = findViewById<TextView>(R.id.menu_list_text)
        val rating = findViewById<RatingBar>(R.id.Rating_menu)
        val image = findViewById<ImageView>(R.id.imageView)
        Glide.with(this).load(val_image).into(image)
        Cuisine_type.text = "Cuisine type : "+val_type
        nom.text = val_nom
        rating.rating = val_rating.toFloat()
        replaceFragment(MenuFragment())

        }
    private fun replaceFragment(homeFragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_menu,homeFragment)
        fragmentTransaction.commit()
    }
}


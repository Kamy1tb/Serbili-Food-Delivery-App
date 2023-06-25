package com.example.cardsprojet.adapters

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardsprojet.databinding.LayoutRestoListItemBinding
import com.example.cardsprojet.models.Restaurant
import com.example.cardsprojet.Menu
import com.example.cardsprojet.R

class MyAdapter(val data: List<Restaurant>,private val context: Context):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutRestoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = data.size

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.binding.apply {
            textName.text = data[position].name
            typeFoodId.text = data[position].type_resto.nom
            Rating.rating= data[position].rating_restaurant
            Adress.text=data[position].location
            logo.setImageResource(R.drawable.resto)
            Glide.with(context).load(data[position].image).into(logo)

            mapsPhoto.setOnClickListener {
                val latitude = data[position].latitude  // Latitude de la position à afficher
                val longitude =  data[position].longitude // Longitude de la position à afficher
                try {
                    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
                    val context = holder.itemView.context
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage("com.google.android.apps.maps")
                    context.startActivity(intent)
                } catch (ex: ActivityNotFoundException) {
                    val mapsUrl = "http://maps.google.com/maps?q=$latitude,$longitude"
                    val context = holder.itemView.context
                    val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
                    context.startActivity(mapsIntent)

                }
            }

            facebookIcon.setOnClickListener{


                val facebookPackageName = "com.facebook.katana"
                val facebookUrl = "https://www.facebook.com/kamyl.tb"
                val context = holder.itemView.context
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("fb://facewebmodal/f?href=$facebookUrl")

                try {
                    // Vérifier si l'application Facebook est installée
                    context.packageManager.getPackageInfo(facebookPackageName, 0)
                    intent.setPackage(facebookPackageName)
                } catch (e: PackageManager.NameNotFoundException) {
                    // Si l'application Facebook n'est pas installée, ouvrir le lien dans le navigateur
                    intent.data = Uri.parse(facebookUrl)
                }

// Lancer l'activité avec l'intent
                context.startActivity(intent)

            }

            holder.binding.restoCard.setOnClickListener{
                val id_resto = data[position].id_resto
                val intent = Intent(context, Menu::class.java)
                intent.putExtra("id_resto",id_resto)
                intent.putExtra("nom_resto",data[position].name)
                intent.putExtra("type_resto",data[position].type_resto.nom)
                intent.putExtra("rating_resto",data[position].rating_restaurant)
                intent.putExtra("image_resto",data[position].image)

                it.context.startActivity(intent)
            }


        }



    }






    class MyViewHolder(val binding: LayoutRestoListItemBinding) : RecyclerView.ViewHolder(binding.root)

}





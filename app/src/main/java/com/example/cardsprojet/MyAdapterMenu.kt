package com.example.cardsprojet

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardsprojet.databinding.LayoutMenuListBinding
import com.example.cardsprojet.models.MenuData
import kotlinx.coroutines.withContext

class MyAdapterMenu(val context: Context, val data: List<MenuData>):RecyclerView.Adapter<MyAdapterMenu.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutMenuListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = data.size

    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.binding.apply {

            nomMenu.text = data[position].nom
            descMenu.text = data[position].description
            prixMenu.text = data[position].prix.toString()+"DA"
            Glide.with(context).load(data[position].image).into(imageMenu)
            imageMenu.setImageResource(R.drawable.sandwich)

        }



    }




    class MyViewHolder(val binding: LayoutMenuListBinding) : RecyclerView.ViewHolder(binding.root)

}




package com.example.cardsprojet.adapters

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardsprojet.Menu
import com.example.cardsprojet.R
import com.example.cardsprojet.databinding.CommandItemBinding
import com.example.cardsprojet.models.CommandSave


class MyAdapterCommand(val data: List<CommandSave>,private val context: Context):RecyclerView.Adapter<MyAdapterCommand.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(CommandItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = data.size

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.binding.apply {

            restonamecommand.text = data[position].nom
            typecuisineinput.text = data[position].typeResto
            totalprixcommand.text = data[position].prix.toString()+" DA"
            if (data[position].status == 0 ){
                statut.text = "En cours"

            }else{
                statut.text = "Terminé"
                statut.setTextColor(Color.GREEN)
            }
            commanddate.text=data[position].date
            commandhour.text=data[position].heure
            logocommand.setImageResource(R.drawable.resto)
            Glide.with(context).load(data[position].image).into(logocommand)

        }



    }






    class MyViewHolder(val binding: CommandItemBinding) : RecyclerView.ViewHolder(binding.root)
}
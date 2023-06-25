package com.example.cardsprojet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardsprojet.DAO.AppDatabase
import com.example.cardsprojet.models.CommandeIT
import com.example.cardsprojet.R
import com.example.cardsprojet.databinding.CartItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MyAdapterCart(val data: ArrayList<CommandeIT>, private val context: Context):RecyclerView.Adapter<MyAdapterCart.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.apply {
             nomMenu.text = data[position].nom
             nbItem.text = data[position].quantite.toString()+" Items"
            prixMenu.text = data[position].prix.toString()+" DA"
            imageCart.setImageResource(R.drawable.resto)
            Glide.with(context).load(data[position].image).into(imageCart)
            }

            holder.binding.delete.setOnClickListener{
                //Toast.makeText(context,data[position].toString()+"position : "+position.toString(),Toast.LENGTH_LONG).show()
                GlobalScope.launch(Dispatchers.IO) {
                    if (position >= 0 && position < data.size) {

                        deleteFromCache(data[position])

                    }
                }
                deleteItem(position)


            }



        }
    private fun deleteItem(position: Int) {

        // Supprimer l'élément de la liste
        if (position >= 0 && position < data.size) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
        // Notifier le RecyclerView du changement

    }

    private fun deleteFromCache(data: CommandeIT){
         var database: AppDatabase;
        database = AppDatabase.getDatabase(context)
        database.commandDao().delete(data)
    }











    class MyViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)

}





package com.example.cardsprojet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardsprojet.DAO.ApiClient
import com.example.cardsprojet.DAO.AppDatabase
import com.example.cardsprojet.adapters.MyAdapter
import com.example.cardsprojet.adapters.MyAdapterCommand
import com.example.cardsprojet.models.CommandSave
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CommandeFragment : Fragment() {

    lateinit var recyclerView : RecyclerView;
    lateinit var database: AppDatabase;
    private lateinit var myView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_commande, container, false)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        recyclerView = requireView().findViewById(R.id.recyclerViewCommand)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        lifecycleScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val user = database.userDao().getUser()
                val commands = user.id_user?.let { loadData(it).await() }
                val progressBar: ProgressBar = view.findViewById(R.id.progressBarCommands)
                CoroutineScope(Dispatchers.Main).launch {
                    progressBar.visibility = View.VISIBLE // Affiche la ProgressBar initialement
                    recyclerView.adapter = context?.let { commands?.let { it1 -> MyAdapterCommand(it1, it) } }
                    progressBar.visibility = View.GONE // Affiche la ProgressBar initialement
                }


            }



        }
    }


    private fun loadData(id_user:Int): Deferred<List<CommandSave>> = CoroutineScope(Dispatchers.IO).async {
        val response = ApiClient.apiService.CommandsById(id_user)
        if (response.isSuccessful) {
            Log.d("ranifelloaddata",response.body().toString())
            val sortedRestaurants = response.body()?.sortedByDescending { it.status}
            sortedRestaurants ?: emptyList()
        } else {
            emptyList()
        }
    }
}
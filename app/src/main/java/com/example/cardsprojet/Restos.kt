package com.example.cardsprojet

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
import com.example.cardsprojet.models.Restaurant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.example.cardsprojet.adapters.MyAdapter
import kotlinx.coroutines.GlobalScope


class Restos : Fragment() {

    lateinit var recyclerView : RecyclerView
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_restos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        recyclerView = requireView().findViewById(R.id.recyclerViewResto)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        viderPanier()
        lifecycleScope.launch {
            val restaurants = loadData().await()

            val progressBar: ProgressBar = view.findViewById(R.id.progressBarResto)
            progressBar.visibility = View.VISIBLE // Affiche la ProgressBar initialement
            recyclerView.adapter = context?.let { MyAdapter(restaurants, it) }
            progressBar.visibility = View.GONE // Affiche la ProgressBar initialement
        }
    }



    private fun loadData(): Deferred<List<Restaurant>> = CoroutineScope(Dispatchers.IO).async {
        val response = ApiClient.apiService.getRestaurants()
        if (response.isSuccessful) {
            val sortedRestaurants = response.body()?.sortedByDescending { it.rating_restaurant}
            sortedRestaurants ?: emptyList()
        } else {
            emptyList()
        }
    }

    private fun viderPanier(){
        GlobalScope.launch(Dispatchers.IO) {
            // Opération d'insertion dans la base de données
            database.commandDao().deleteAll()
            val all = database.commandDao().getAll()
            Log.d("listehh",all.toString())

        }
    }



}
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
import com.example.cardsprojet.models.MenuData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = requireView().findViewById(R.id.recyclerViewMenu)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        lifecycleScope.launch {
            val menus = loadData(2).await()
            val progressBar: ProgressBar = view.findViewById(R.id.progressBarMenu)
            progressBar.visibility = View.VISIBLE // Affiche la ProgressBar initialement
            recyclerView.adapter = context?.let { MyAdapterMenu(it, menus) }
            progressBar.visibility = View.GONE

        }
    }



    private fun loadData(id_resto: Int): Deferred<List<MenuData>> = CoroutineScope(Dispatchers.IO).async {
        val response = ApiClient.apiService.menuByID(id_resto)
        if (response.isSuccessful) {


            response.body() ?: emptyList()

        } else {
            emptyList()
        }
    }





}
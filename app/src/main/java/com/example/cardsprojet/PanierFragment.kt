package com.example.cardsprojet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardsprojet.DAO.AppDatabase
import com.example.cardsprojet.models.CommandeIT
import com.example.cardsprojet.adapters.MyAdapterCart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class PanierFragment : Fragment() {

    lateinit var recyclerView : RecyclerView;
    lateinit var database: AppDatabase;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panier, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        recyclerView = requireView().findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        lifecycleScope.launch {
            val menus = loadData().await()
            val progressBar: ProgressBar = view.findViewById(R.id.progressBarCart)
            progressBar.visibility = View.VISIBLE // Affiche la ProgressBar initialement
            recyclerView.adapter = context?.let { MyAdapterCart(menus,it) }
            progressBar.visibility = View.GONE
        }
    }

    private fun loadData(): Deferred<ArrayList<CommandeIT>> = CoroutineScope(Dispatchers.IO).async {
        val response = database.commandDao().getAll()
        (if (! response.isEmpty()) {
            val arrayList: ArrayList<CommandeIT> = ArrayList(response)
            arrayList

        } else {
            emptyList()
        }) as ArrayList<CommandeIT>
    }


}
package com.example.cardsprojet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardsprojet.DAO.ApiClient
import com.example.cardsprojet.DAO.AppDatabase
import com.example.cardsprojet.models.CommandeIT
import com.example.cardsprojet.adapters.MyAdapterCart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Calendar


class PanierFragment : Fragment() {
    private lateinit var myView: View

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
        myView = inflater.inflate(R.layout.fragment_panier, container, false)
        return myView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        recyclerView = requireView().findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        lifecycleScope.launch {
            GlobalScope.launch(Dispatchers.IO) {
                val getPrix = database.commandDao().getTotal()
            }
            val menus = loadData().await()
            val progressBar: ProgressBar = view.findViewById(R.id.progressBarCart)
            progressBar.visibility = View.VISIBLE // Affiche la ProgressBar initialement
            val adapter = MyAdapterCart(menus,requireContext(),this@PanierFragment)

            recyclerView.adapter = adapter

            progressBar.visibility = View.GONE

            val validate: Button = view.findViewById(R.id.validerpanier)

            validate.setOnClickListener{
                lifecycleScope.launch {
                    GlobalScope.launch(Dispatchers.IO) {
                        val result = insertCart(requireArguments().getInt("id_resto"),1F).await()
                        for (item in adapter.data){
                            val fin = result?.let { it1 -> item.id_menu?.let { it2 ->
                                insertCartItems(it1,
                                    it2,item.quantite).await()
                            } }
                        }


                    }

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)


                    /*for (menu in menus) {

                    }*/

                }
            }
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










    private suspend fun insertCart(id_resto:Int,prix:Float): Deferred<Int?> = CoroutineScope(Dispatchers.IO).async {
            val user =database.userDao().getUser()
            val jsonData = mapOf(
                "id_resto" to id_resto.toString(),
                "id_user" to user.id_user.toString(),
                "total" to prix.toString(),
                "date" to Calendar.getInstance().time.toString()
                )
            //try{
                val response = ApiClient.apiService.newCommand(jsonData)
                response.body()

    }



    private suspend fun insertCartItems(id_command: Int, id_item:Int, quantite: Int?) =
        CoroutineScope(Dispatchers.IO).async {
            val jsonData = mapOf(
                "id_command" to id_command.toString(),
                "id_item" to id_item.toString(),
                "quantite" to quantite.toString(),

            )
            try{
                val response = ApiClient.apiService.newCommandItems(jsonData)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorJson = JSONObject(errorResponse)
                    val errorMessage = errorJson.getString("detail")
                    errorMessage
                }

            } catch (e : Exception){
                e.printStackTrace()
            }


        }







}



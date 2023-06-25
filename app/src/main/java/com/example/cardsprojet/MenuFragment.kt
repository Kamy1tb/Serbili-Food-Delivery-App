package com.example.cardsprojet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardsprojet.DAO.ApiClient
import com.example.cardsprojet.DAO.AppDatabase
import com.example.cardsprojet.models.CommandeIT
import com.example.cardsprojet.adapters.MyAdapterMenu
import com.example.cardsprojet.models.MenuData
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {

    lateinit var recyclerView : RecyclerView;
    lateinit var bottomSheetDialog: BottomSheetDialog;
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

        return inflater.inflate(R.layout.fragment_menu, container, false)
    }
    override fun onDestroyView() {
        // Supprimer les données mises en cache ici
        // Réinitialiser les valeurs, supprimer la mémoire cache, annuler les tâches, etc.
        viderPanier()

        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        bottomSheetDialog = context?.let { BottomSheetDialog(it) }!!;

        val layoutManager = LinearLayoutManager(context)
        recyclerView = requireView().findViewById(R.id.recyclerViewMenu)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        lifecycleScope.launch {

            val menus = arguments?.let { loadData(it.getInt("id_resto")).await() }
            val progressBar: ProgressBar = view.findViewById(R.id.progressBarMenu)
            val panier: View = view.findViewById(R.id.cartview)
            panier.visibility = View.GONE
            progressBar.visibility = View.VISIBLE // Affiche la ProgressBar initialement
            recyclerView.adapter = context?.let { menus?.let { it1 -> MyAdapterMenu(it, it1) } }
            progressBar.visibility = View.GONE
            recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                @SuppressLint("MissingInflatedId", "SetTextI18n")
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (e.action == MotionEvent.ACTION_UP) {
                        val childView = rv.findChildViewUnder(e.x, e.y)
                        val position = childView?.let { rv.getChildAdapterPosition(it) }
                        position?.let {
                            val adapter = recyclerView.adapter
                            val item = menus?.get(position)
                            val bottomSheetDialog = BottomSheetDialog(requireContext())
                            val view = layoutInflater.inflate(R.layout.add_card_layout, null)
                            val textView1 = view.findViewById<TextView>(R.id.menu_cart_name)
                            val textView2 = view.findViewById<TextView>(R.id.prix_menu)
                            val textView3 = view.findViewById<TextView>(R.id.nombremenu)
                            val plus = view.findViewById<Button>(R.id.buttonplus)
                            val moins = view.findViewById<Button>(R.id.buttonmoins)
                            val valider = view.findViewById<Button>(R.id.valider)

                            textView1.text = item!!.nom
                            textView2.text = item.prix.toString()+" DA"

                            plus.setOnClickListener{
                                textView3.text = (textView3.text.toString().toInt()+1).toString()
                                textView2.text = (textView2.text.toString().replace(Regex("[^0-9]"), "").toInt() + item!!.prix).toString()+" DA"
                            }
                            moins.setOnClickListener{
                                if (textView3.text.toString().toInt() > 1){
                                    textView3.text = (textView3.text.toString().toInt()-1).toString()
                                    textView2.text = (textView2.text.toString().replace(Regex("[^0-9]"), "").toInt() - item!!.prix).toString()+" DA"
                                }

                            }
                            valider.setOnClickListener{
                                item?.let { it1 -> WriteData(it1.id_menu,item.nom,textView3.text.toString().toInt(),item.prix.toFloat(),item.image) }
                                Toast.makeText(requireContext(),"Successfully added to cart",Toast.LENGTH_SHORT).show()
                                updatePanier()
                                panier.visibility = View.VISIBLE
                                bottomSheetDialog.hide()

                            }

                            panier.setOnClickListener{
                                CoroutineScope(Dispatchers.Main).launch {
                                    //Toast.makeText(this@MainActivity,verifyCache().await().toString(),Toast.LENGTH_LONG).show()
                                    if (verifyCache().await().toInt() == 0) {
                                        val intent = Intent(requireContext(), Authentification::class.java)
                                        startActivity(intent)
                                    }
                                    else{
                                        val fragment = PanierFragment()
                                        val bundle = Bundle()
                                        arguments?.let { it1 ->
                                            bundle.putInt("id_resto",
                                                it1.getInt("id_resto"))
                                        }
                                        fragment.arguments = bundle
                                        val transaction = fragmentManager?.beginTransaction()
                                        transaction?.replace(R.id.navigation,fragment)?.commit()

                                    }
                                }



                            }


                            bottomSheetDialog.setContentView(view)
                            bottomSheetDialog.show()
                            // Handle the clicked position here
                            // Example: Log the position
                            Log.d("YourFragment", "Clicked position: $item")
                        }
                    }
                    return false
                }



                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })

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

    private fun WriteData(id_menu: Int,name:String, quantite:Int,prix:Float, image: String){
        val commande =  CommandeIT(id_menu,name,prix,quantite,image)
        GlobalScope.launch(Dispatchers.IO) {
            // Opération d'insertion dans la base de données
            database.commandDao().insert(commande)
            val all = database.commandDao().getAll()


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

    private fun getItemCount(callback: (count: Int) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            // Opération d'insertion dans la base de données
            database.commandDao().deleteAll()
            val all = database.commandDao().getCount()
            callback(all) // Appeler le rappel avec la valeur obtenue
        }
    }

    private fun updatePanier(){
        GlobalScope.launch(Dispatchers.IO) {
           val count =  database.commandDao().getCount()
            val prix = database.commandDao().getTotal()
            val panier_count = requireView().findViewById<TextView>(R.id.count)
            val panier_prix = requireView().findViewById<TextView>(R.id.prix_cart)
            requireActivity().runOnUiThread{
                panier_count.text = count.toString()+" product"
                panier_prix.text = prix.toString()+" DA"

            }

        }
    }

    private suspend fun verifyCache()=
        CoroutineScope(Dispatchers.IO).async {
            database.userDao().verifyLogged()
        }






}
package com.example.cardsprojet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardsprojet.models.MenuData
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {

    lateinit var recyclerView : RecyclerView;
    lateinit var bottomSheetDialog: BottomSheetDialog;

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
        bottomSheetDialog = context?.let { BottomSheetDialog(it) }!!;

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
            recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (e.action == MotionEvent.ACTION_UP) {
                        val childView = rv.findChildViewUnder(e.x, e.y)
                        val position = childView?.let { rv.getChildAdapterPosition(it) }
                        position?.let {
                            val adapter = recyclerView.adapter
                            val item = menus[position]
                            val bottomSheetDialog = BottomSheetDialog(requireContext())
                            val view = layoutInflater.inflate(R.layout.add_card_layout, null)

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






}
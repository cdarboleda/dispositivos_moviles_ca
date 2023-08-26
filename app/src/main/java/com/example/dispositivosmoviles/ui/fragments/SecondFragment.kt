package com.example.dispositivosmoviles.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.databinding.FragmentSecondBinding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.logic.marvelLogic.MarvelLogic
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.data.entities.marvel.characters.adapters.MarvelAdapter2
import com.example.dispositivosmoviles.logic.data.convertMarvelCharsNormaltoDB
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem2
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
import com.example.dispositivosmoviles.ui.utilities.Metodos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding;
    private lateinit var rvAdapter: MarvelAdapter2;
    private lateinit var lManager: LinearLayoutManager
    private lateinit var gManager: GridLayoutManager
    private val limit = 10
    private var offset = 0
    private var marvelCharsItems: MutableList<MarvelChars> = mutableListOf<MarvelChars>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSecondBinding.inflate(
            layoutInflater, container, false)

        rvAdapter = MarvelAdapter2(
            { item -> sendMarvelItem(item) },
            { item -> deleteMarvelFavItem(item.id) }
        )
        retainInstance = true
        lManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        chargeDataRVInit()
        gManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        return binding.root    }

    override fun onStart() {
        super.onStart();
        binding.rvSwipe.setOnRefreshListener {
            chargeDataRVInit()
            binding.rvSwipe.isRefreshing = false
            //lManager.scrollToPositionWithOffset(5, 20)
        }
        binding.btnUp.setOnClickListener {
            binding.rvMarvelChars.scrollToPosition(0)
        }

        binding.txtFilter.addTextChangedListener { filteredText ->
            val newItems = marvelCharsItems.filter { items ->
                items.name.lowercase().contains(filteredText.toString().lowercase())
            }

            rvAdapter.replaceListAdapter(newItems)
        }

    }

    fun sendMarvelItem(item: MarvelChars) {
        //Intent(contexto de la activity, .class de la activity)
        val i = Intent(requireActivity(), DetailsMarvelItem2::class.java)
        i.putExtra("item", item)//mandamos los items a la otra activity
        startActivity(i)
    }

    fun deleteMarvelFavItem(id: Int) :Boolean{
        //Intent(contexto de la activity, .class de la activity)
        lifecycleScope.launch(Dispatchers.Main){
            withContext(Dispatchers.IO){
                MarvelLogic().deleteMarvelFavChartoDB(id, requireActivity())
            }
        }

        return true
    }

    fun chargeDataRVInit() {
        if (Metodos().isOnline(requireActivity())) {
            lifecycleScope.launch(Dispatchers.Main) {
                marvelCharsItems = withContext(Dispatchers.IO) {
                    return@withContext MarvelLogic().getInitFavChars(requireActivity())
                }
                rvAdapter.items = marvelCharsItems

                binding.rvMarvelChars.apply {
                    this.adapter = rvAdapter;
                    this.layoutManager = gManager;
                    //gManager.scrollToPositionWithOffset(limit, 10)
                }
            }
        }

    }
}
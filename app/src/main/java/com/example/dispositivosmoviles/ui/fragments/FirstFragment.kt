package com.example.dispositivosmoviles.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dispositivosmoviles.databinding.FragmentFirstBinding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.logic.marvelLogic.MarvelLogic
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.data.entities.marvel.characters.adapters.MarvelAdapter
import com.example.dispositivosmoviles.ui.utilities.Metodos
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding;

    private var rvAdapter: MarvelAdapter = MarvelAdapter(
        { item -> sendMarvelItem(item) },
        { item -> saveMarvelItem(item) }
    )
    private lateinit var lManager: LinearLayoutManager
    private lateinit var gManager: GridLayoutManager
    private val limit = 10
    private var offset = 0
    private var marvelCharsItems: MutableList<MarvelChars> = mutableListOf<MarvelChars>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(layoutInflater, container, false)
        lManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        retainInstance = true
        chargeDataRVInitComplete()
        //Dos por fila
        gManager = GridLayoutManager(requireActivity(), 3)
        return binding.root

    }

    override fun onStart() {
        super.onStart();

        binding.rvSwipe.setOnRefreshListener {
            chargeDataRVInitComplete()
            binding.rvSwipe.isRefreshing = false
            lManager.scrollToPositionWithOffset(0, 20)
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
        val i = Intent(requireActivity(), DetailsMarvelItem::class.java)
        i.putExtra("item", item)//mandamos los items a la otra activity
        startActivity(i)
    }

    fun saveMarvelItem(item: MarvelChars) :Boolean{
        //Intent(contexto de la activity, .class de la activity)
        lifecycleScope.launch(Dispatchers.Main){
            withContext(Dispatchers.IO){
                MarvelLogic().insertMarvelFavChartoDB(item, requireActivity())

            }
        }

        return item !== null
    }

    //Es para consultar del API directamente
    fun chargeDataRVAPI(search: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            //rvAdapter.items = JikanAnimeLogic().getAllAnimes()
            marvelCharsItems = withContext(Dispatchers.IO) {
                return@withContext MarvelLogic().getMarvelCharsApiStartsWith(search,limit)
            }
            rvAdapter.items = marvelCharsItems

            binding.rvMarvelChars.apply {
                this.adapter = rvAdapter;
                this.layoutManager = gManager;
                //gManager.scrollToPositionWithOffset(0, 10)
            }
            this@FirstFragment.offset += limit
        }
    }

    //Leemos los elementos de la base de datos, si no hay ninguno consultamos del API normal y le añadimos a la base de datos
    //y por ende tambien al recycler view
    fun chargeDataRVInit(offset: Int,limit: Int ) {
        if (Metodos().isOnline(requireActivity())) {
            lifecycleScope.launch(Dispatchers.Main) {
                marvelCharsItems = withContext(Dispatchers.IO) {
                    return@withContext MarvelLogic().getInitChars(offset,limit, requireActivity())
                    //return@withContext MarvelLogic().getAllMarvelCharsApi(offset, limit,requireActivity())
                }
                this@FirstFragment.offset+=limit
                rvAdapter.items = marvelCharsItems

                binding.rvMarvelChars.apply {
                    this.adapter = rvAdapter;
                    this.layoutManager = gManager;
                    gManager.scrollToPositionWithOffset(limit, offset)
                }
            }
        }

    }

    fun chargeDataRVInitComplete() {
        if (Metodos().isOnline(requireActivity())) {
            binding.progressBar2.visibility = View.VISIBLE
            lifecycleScope.launch(Dispatchers.Main) {
                marvelCharsItems = withContext(Dispatchers.IO) {
                    return@withContext MarvelLogic().getAllMarvelCharsApiInit(requireActivity())
                }
                //this@FirstFragment.offset+=limit
                rvAdapter.items = marvelCharsItems

                binding.rvMarvelChars.apply {
                    this.adapter = rvAdapter;
                    this.layoutManager = gManager;
                    gManager.scrollToPositionWithOffset(limit, offset)
                }

                binding.progressBar2.visibility = View.GONE
            }
        }

    }
}
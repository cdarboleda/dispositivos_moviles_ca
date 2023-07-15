package com.example.dispositivosmoviles.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.databinding.FragmentFirstBinding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.logic.marvelLogic.MarvelLogic
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.data.entities.marvel.characters.adapters.MarvelAdapter
import com.example.dispositivosmoviles.logic.data.getMarvelCharsDB
import com.example.dispositivosmoviles.ui.utilities.DispositivosMoviles
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
        // Inflate the layout for this fragment

        binding = FragmentFirstBinding.inflate(
            layoutInflater, container, false
        )


        lManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )

        //Dos por fila
        gManager = GridLayoutManager(requireActivity(), 2)
        return binding.root

    }

    override fun onStart() {
        super.onStart();


        val names = arrayListOf<String>("A", "B", "C", "D", "E")

        val adapter1 = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            names
        )

        binding.spinner.adapter = adapter1
        chargeDataRVInit(limit, offset)

        binding.rvSwipe.setOnRefreshListener {
            chargeDataRVInit(limit, offset)
            binding.rvSwipe.isRefreshing = false
            lManager.scrollToPositionWithOffset(5, 20)
        }

        binding.rvMarvelChars.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dx > 0) {
                        val v = lManager.childCount//Cuantos han pasado
                        val p = lManager.findFirstVisibleItemPosition()//Cual es mi posicion actual
                        val t = lManager.itemCount//Cuantos tengo en total

                        if ((v + p) >= t) {
                            lifecycleScope.launch((Dispatchers.Main))
                            {
                                val x = with(Dispatchers.IO) {
                                    MarvelLogic().getAllMarvelChars(0, limit)
                                    //JikanAnimeLogic().getAllAnimes()
                                }
                                rvAdapter.updateListAdapter((x))
                                this@FirstFragment.offset += offset

                            }
                        }

                    }

                }
            })

        binding.txtFilter.addTextChangedListener { filteredText ->
            val newItems = marvelCharsItems.filter { items ->
                items.name.lowercase().contains(filteredText.toString().lowercase())
            }

            rvAdapter.replaceListAdapter(newItems)
        }

    }

    fun corrotine() {
        lifecycleScope.launch(Dispatchers.Main) {
            var name = "Bayron"

            name = withContext(Dispatchers.IO)
            {
                name = "Jairo"
                return@withContext name
            }

            binding.cardView.radius
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
                DispositivosMoviles
                    .getDbInstance()
                    .marvelDao()
                    .insertMarvelCharacter(listOf( item.getMarvelCharsDB()))
            }
        }

            return true
    }


    fun chargeDataRVAPI(limit: Int, offset: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            //rvAdapter.items = JikanAnimeLogic().getAllAnimes()
            marvelCharsItems = withContext(Dispatchers.IO) {
                return@withContext MarvelLogic().getAllMarvelChars(offset,limit).toMutableList()
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

    fun chargeDataRVInit(limit: Int, offset: Int) {

        if (Metodos().isOnline(requireActivity())) {
            lifecycleScope.launch(Dispatchers.Main) {

                marvelCharsItems = withContext(Dispatchers.IO) {

                    /* var items = MarvelLogic().getAllMarvelCharDB().toMutableList()

                     if (items.isEmpty()) {
                         items = (MarvelLogic().getAllMarvelChars(
                             0, 99
                         ))
                         MarvelLogic().insertMarvelCharstoDB(items)

                     }*/
                    //Utilizando las capas, el método de arriba fue cambiado a la capa Logic (MarvelLogic)
                    return@withContext MarvelLogic().getInitChars(offset,limit)
                }


                /*withContext(Dispatchers.IO){
            }
            */
                this@FirstFragment.offset+=limit
                rvAdapter.items = marvelCharsItems

                binding.rvMarvelChars.apply {
                    this.adapter = rvAdapter;
                    this.layoutManager = gManager;


                    gManager.scrollToPositionWithOffset(limit, 10)
                }
            }
        }

    }
}
package com.example.dispositivosmoviles.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.data.entities.jikan.JikanAnimeEntity
import com.example.dispositivosmoviles.data.entities.jikan.characters.adapters.JikanAdapter
import com.example.dispositivosmoviles.data.entities.marvel.characters.adapters.MarvelAdapter
import com.example.dispositivosmoviles.databinding.FragmentThirdBinding
import com.example.dispositivosmoviles.logic.data.JikanChars
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.logic.jikanLogic.JikanAnimeLogic
import com.example.dispositivosmoviles.logic.marvelLogic.MarvelLogic
import com.example.dispositivosmoviles.ui.activities.DetailsJikanItem
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.ui.utilities.Metodos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThirdFragment : Fragment() {

    private lateinit var binding: FragmentThirdBinding;

    private var rvAdapter: JikanAdapter = JikanAdapter({ item -> sendMarvelItem(item) })
    private lateinit var lManager: LinearLayoutManager;
    private var jikanItems: MutableList<JikanChars> = mutableListOf<JikanChars>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentThirdBinding.inflate(layoutInflater, container, false)


        lManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        chargeDataRVInit()
        return (binding.root)
    }

    override fun onStart() {
        super.onStart();

        binding.rvSwipe.setOnRefreshListener {

            binding.rvSwipe.isRefreshing = false
            lManager.scrollToPositionWithOffset(5, 20)
        }

        binding.rvJikanChars.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val v = lManager.childCount//Cuantos han pasado
                        val p = lManager.findFirstVisibleItemPosition()//Cual es mi posicion actual
                        val t = lManager.itemCount//Cuantos tengo en total

                        if ((v + p) >= t) {
                            lifecycleScope.launch((Dispatchers.Main))
                            {
                                val x = with(Dispatchers.IO) {
                                    JikanAnimeLogic().getAllAnimes()
                                }
                                rvAdapter.updateListAdapter((x))

                            }
                        }

                    }

                }
            })
    }

    fun chargeDataRVInit() {

        if (Metodos().isOnline(requireActivity())) {

            //binding.shimmerLayout.visibility = View.VISIBLE
            lifecycleScope.launch(Dispatchers.Main) {
                jikanItems = withContext(Dispatchers.IO) {
                    return@withContext JikanAnimeLogic().getAllAnimes()
                }
                rvAdapter.items = jikanItems

                binding.rvJikanChars.apply {
                    this.adapter = rvAdapter;
                    this.layoutManager = lManager;

                }

                //binding.shimmerLayout.visibility = View.GONE
            }


        }

    }

    fun sendMarvelItem(item: JikanChars) {
        //Intent(contexto de la activity, .class de la activity)
        val i = Intent(requireActivity(), DetailsJikanItem::class.java)
        i.putExtra("item", item)//mandamos los items a la otra activity
        startActivity(i)
    }
}
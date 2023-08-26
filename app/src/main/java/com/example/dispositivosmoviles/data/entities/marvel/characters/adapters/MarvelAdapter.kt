package com.example.dispositivosmoviles.data.entities.marvel.characters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.databinding.MarvelCharactersBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class MarvelAdapter(
    //private var items: List<MarvelChars>,
    private var fnClick: (MarvelChars) -> Unit,
    private var fnSave: (MarvelChars) -> Boolean
) :
    RecyclerView.Adapter<MarvelAdapter.MarvelViewHolder>() {
    var items: List<MarvelChars> = listOf()
    class MarvelViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: MarvelCharactersBinding = MarvelCharactersBinding.bind(view)

        fun render(
            item: MarvelChars,
            fnClick: (MarvelChars) -> Unit,
            fnSave: (MarvelChars) -> Boolean
        ) {
            binding.txtName.text = item.name;
            //binding.txtComic.text = item.comic;
            Picasso.get().load(item.image).into(binding.imgImage)

            itemView.setOnClickListener{ fnClick(item) }
            binding.btnDelete.setOnClickListener { fnSave(item) }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarvelViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MarvelViewHolder(
            inflater.inflate(
                R.layout.marvel_characters,
                parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: MarvelViewHolder, position: Int) {
        holder.render(items[position], fnClick, fnSave)
    }

    override fun getItemCount(): Int = items.size

    fun updateListAdapter(newitems : List<MarvelChars>){
        this.items = this.items.plus(newitems)
        notifyDataSetChanged()
    }

    fun replaceListAdapter(newitems : List<MarvelChars>){
        this.items = newitems
        notifyDataSetChanged()
    }

}
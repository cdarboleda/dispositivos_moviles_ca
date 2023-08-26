package com.example.dispositivosmoviles.data.entities.marvel.characters.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.FragmentSecondBinding
import com.example.dispositivosmoviles.databinding.MarvelCharacters2Binding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.squareup.picasso.Picasso
import java.lang.Thread.sleep

class MarvelAdapter2 (
    private var fnClick: (MarvelChars) -> Unit,
    private var fnDelete: (MarvelChars) -> Boolean
    ) :
    RecyclerView.Adapter<MarvelAdapter2.MarvelViewHolder>()
    {
        var items: List<MarvelChars> = listOf()

        class MarvelViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val binding: MarvelCharacters2Binding = MarvelCharacters2Binding.bind(view)
            fun render(
                item: MarvelChars,
                fnClick: (MarvelChars) -> Unit,
                fnDelete: (MarvelChars) -> Boolean
            ) {
                binding.txtName.text = item.name;
                Picasso.get().load(item.image).into(binding.imgImage)

                itemView.setOnClickListener {
                    fnClick(item)
                }
                binding.btnDelete.setOnClickListener {
                    fnDelete(item)
                }
            }


        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MarvelViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return MarvelViewHolder(
                inflater.inflate(
                    R.layout.marvel_characters2,
                    parent, false
                )
            )

        }

        override fun onBindViewHolder(holder: MarvelViewHolder, position: Int) {
            holder.render(items[position], fnClick, fnDelete)
        }

        override fun getItemCount(): Int = items.size

        fun updateListAdapter(newitems: List<MarvelChars>) {
            this.items = this.items.plus(newitems)
            notifyDataSetChanged()
        }

        fun replaceListAdapter(newitems: List<MarvelChars>) {
            this.items = newitems
            notifyDataSetChanged()
        }

    }

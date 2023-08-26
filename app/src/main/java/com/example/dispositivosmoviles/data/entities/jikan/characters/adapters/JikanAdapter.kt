package com.example.dispositivosmoviles.data.entities.jikan.characters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.JikanCharactersBinding
import com.example.dispositivosmoviles.logic.data.JikanChars
import com.squareup.picasso.Picasso

class JikanAdapter(
    private var fnClick: (JikanChars) -> Unit,
) :
    RecyclerView.Adapter<JikanAdapter.JikanViewHolder>() {
    var items: List<JikanChars> = listOf()

    class JikanViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val binding: JikanCharactersBinding = JikanCharactersBinding.bind(view)

        fun render(
            item: JikanChars,
            fnClick: (JikanChars) -> Unit
        ) {
            binding.txtName.text = item.name;
            binding.txtSyn.text = item.synopsis;
            Picasso.get().load(item.image).into(binding.imgImage)

            itemView.setOnClickListener{
                fnClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JikanViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return JikanViewHolder(
            inflater.inflate(
                R.layout.jikan_characters,
                parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: JikanViewHolder, position: Int) {
        holder.render(items[position], fnClick)
    }

    override fun getItemCount(): Int = items.size

    fun updateListAdapter(newitems : List<JikanChars>){
        this.items = this.items.plus(newitems)
        notifyDataSetChanged()
    }
}
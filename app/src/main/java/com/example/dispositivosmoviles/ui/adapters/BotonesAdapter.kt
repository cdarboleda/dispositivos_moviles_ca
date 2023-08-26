package com.example.dispositivosmoviles.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.data.entities.Funcionalidad
import com.example.dispositivosmoviles.databinding.FuncionalidadCardBinding
import com.example.dispositivosmoviles.logic.data.MarvelChars
import com.example.dispositivosmoviles.ui.activities.DetailsMarvelItem
import com.example.dispositivosmoviles.ui.activities.PrincipalActivity
import com.squareup.picasso.Picasso

class BotonesAdapter : RecyclerView.Adapter<BotonesAdapter.BotonesViewHolder>() {
    var items: List<Funcionalidad> = listOf()

    class BotonesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: FuncionalidadCardBinding = FuncionalidadCardBinding.bind(view)

        fun render(item: Funcionalidad, context: Context, color: Int) {
            binding.crdFuncionalidadNombre.text = item.titulo;
            binding.crdFuncionalidadDescripcion.text = item.descripcion;

            val resourceId = context.resources.getIdentifier(
                item.imagen,
                "drawable",
                context.packageName
            )

            binding.crdFuncionalidadImagen.setImageResource(resourceId)
            //binding.crdFuncionalidad.setCardBackgroundColor(color)
            binding.crdFuncionalidad.setOnClickListener {
                val i = Intent(context, Class.forName(item.nombreClass))
                context.startActivity(i)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BotonesAdapter.BotonesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BotonesAdapter.BotonesViewHolder(
            inflater.inflate(
                R.layout.funcionalidad_card,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BotonesViewHolder, position: Int) {
        val colors = listOf<Int>(
            ContextCompat.getColor(holder.itemView.context, R.color.blue),
            ContextCompat.getColor(holder.itemView.context, R.color.blue2),
            ContextCompat.getColor(holder.itemView.context, R.color.blue3),
            ContextCompat.getColor(holder.itemView.context, R.color.blue4),
            ContextCompat.getColor(holder.itemView.context, R.color.red),
            ContextCompat.getColor(holder.itemView.context, R.color.red2),
            ContextCompat.getColor(holder.itemView.context, R.color.red3),
            ContextCompat.getColor(holder.itemView.context, R.color.red4),
            ContextCompat.getColor(holder.itemView.context, R.color.purple),
            ContextCompat.getColor(holder.itemView.context, R.color.purple2),
            ContextCompat.getColor(holder.itemView.context, R.color.purple3),
            ContextCompat.getColor(holder.itemView.context, R.color.purple4)
        )

        holder.render(items[position], holder.itemView.context, colors.random() - 1)


    }
}


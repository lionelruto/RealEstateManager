package com.openclassrooms.realestatemanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.databinding.RealestateItemsBinding
import com.openclassrooms.realestatemanager.views.ListItemListener

class EstateAdapter constructor(private val estateList: ArrayList<Estate>, private val listItemListener: ListItemListener) : RecyclerView.Adapter<EstateAdapter.EstateViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateViewHolder {
        val view= RealestateItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EstateViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {

        val estate: Estate = estateList[position]
        holder.bind(estate)
    }

    override fun getItemCount(): Int = estateList.size


    inner class EstateViewHolder(private val bind: RealestateItemsBinding) : RecyclerView.ViewHolder(bind.root) {


        fun bind( estate: Estate){
            bind.estateName.text= estate.description
            bind.estateLocation.text= estate.adress
            bind.estatePrice.text= estate.price.toString()

        }

        init {
            bind.root.setOnClickListener{
                val clikedposition= adapterPosition
                listItemListener.onItemclik(clikedposition)


            }
        }





    }
}




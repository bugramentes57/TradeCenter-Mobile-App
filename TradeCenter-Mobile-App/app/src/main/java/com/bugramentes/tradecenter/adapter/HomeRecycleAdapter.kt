package com.bugramentes.tradecenter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugramentes.tradecenter.databinding.UrunListBinding
import com.bugramentes.tradecenter.model.Urun
import com.squareup.picasso.Picasso

class HomeRecycleAdapter(val urunList : ArrayList<Urun>) : RecyclerView.Adapter<HomeRecycleAdapter.UrunHolder>() {


    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener=clickListener
    }


    class UrunHolder(val binding : UrunListBinding, clickListener: onItemClickListener  ) : RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrunHolder {
        val binding = UrunListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UrunHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: UrunHolder, position: Int) {
        holder.binding.recyclerEmailText.text = urunList.get(position).email
        holder.binding.bilgiText.text = urunList.get(position).urunBilgi
        Picasso.get().load(urunList.get(position).downloadUrl).into(holder.binding.urunImage)



    }

    override fun getItemCount(): Int {
        return urunList.size
    }
}
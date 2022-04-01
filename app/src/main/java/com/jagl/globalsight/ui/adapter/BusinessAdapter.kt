package com.jagl.globalsight.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.jagl.globalsight.data.model.Businesse
import com.jagl.globalsight.data.model.ResponseYelpApi
import com.jagl.globalsight.databinding.LayoutMainBinding
import com.jagl.globalsight.ui.view.DetaillActivity
import com.jagl.globalsight.util.setImage

class BusinessAdapter(val data: ResponseYelpApi?, val context: Context) :
    RecyclerView.Adapter<BusinessAdapter.ViewHolder>() {

    private val TAG = BusinessAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutMainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bussines = data?.businesses?.get(position)
        holder.bind(bussines ?: null)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetaillActivity::class.java)
                .putExtra(DetaillActivity.BUSSINES, bussines)
                .putExtra(DetaillActivity.COORDINATES, data?.region?.center)
            Log.d(TAG, "Intent set\n$bussines")
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = data?.businesses?.count() ?: 0

    inner class ViewHolder(private val binding: LayoutMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(business: Businesse?) {
            when (business) {
                null -> {
                    binding.root.visibility = View.GONE
                }
                else -> {
                    binding.name.text = "Name : ${business.name}"
                    binding.category.text = "Category : ${business.categories?.get(0)?.title}"
                    binding.direcction.text = "Direcction : ${business.location?.address1}"
                    binding.image.setImage(business.image_url ?: "")
                    when (binding.root.visibility) {
                        View.GONE -> View.VISIBLE
                    }
                }
            }
        }

    }


}
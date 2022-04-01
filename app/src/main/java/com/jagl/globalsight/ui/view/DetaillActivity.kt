package com.jagl.globalsight.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jagl.globalsight.data.model.Businesse
import com.jagl.globalsight.data.model.Coordinates
import com.jagl.globalsight.databinding.ActivityDetaillBinding
import com.jagl.globalsight.util.setImage

class DetaillActivity : AppCompatActivity() {

    companion object {
        const val BUSSINES = "detaill_activity_bussines"
        const val COORDINATES = "detaill_activity_coordinates"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetaillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Businesse>(BUSSINES)
        val user = intent.getParcelableExtra<Coordinates>(COORDINATES)
        data?.let {
            val bussines = Coordinates(it.coordinates?.latitude, it.coordinates?.longitude)
            with(binding) {
                image.setImage(it.image_url ?: "")
                name.text = "Name: ${it.name}"
                category.text = "Category: ${it.categories?.get(0)?.title}"
                direcction.text = "Adress: ${it.location?.address1}"
                rating.rating = it.rating?.toFloat() ?: 0.0f
                price.text = "Price: ${it.price}"
                phone.text = "Phone: ${it.phone}"
                mapBtn.setOnClickListener { onMapClick(user, bussines) }
            }
        }
    }

    private fun onMapClick(user: Coordinates?, bussines: Coordinates) {
        val intent = Intent(this, MapActivity::class.java)
            .putExtra(MapActivity.BUSSINES_COORDINATES, bussines)
            .putExtra(MapActivity.USER_COORDINATES, user?:bussines)
        startActivity(intent)
    }
}
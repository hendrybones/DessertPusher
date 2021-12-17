package com.example.dessertpusher

import android.app.Activity
import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import com.example.dessertpusher.databinding.ActivityMainBinding
import timber.log.Timber
const val KEY_REVENUE="key_revenue"
const val KEY_DESSERTS_SOLD="key_dessertsSold"
class MainActivity : AppCompatActivity(){


    private var  revenue=0
    private var dessertsSold=0
    private lateinit var desssertTimer: DesssertTimer

    //

    //contains all views
    private  lateinit var binding: ActivityMainBinding

    //dessert data class
    data class Dessert(val imageId:Int, val price: Int,val startProductionAmount: Int)

    private val allDesserts= listOf(
        Dessert(R.drawable.cupcake, 5, 0),
        Dessert(R.drawable.donut, 10, 5),
        Dessert(R.drawable.eclair, 15, 20),
        Dessert(R.drawable.froyo, 30, 50),
        Dessert(R.drawable.gingerbread, 50, 100),
        Dessert(R.drawable.honeycomb, 100, 200),
        Dessert(R.drawable.icecreamsandwich, 500, 500),
        Dessert(R.drawable.jellybean, 1000, 1000),
        Dessert(R.drawable.kitkat, 2000, 2000),
        Dessert(R.drawable.lollipop, 3000, 4000),
        Dessert(R.drawable.marshmallow, 4000, 8000),
        Dessert(R.drawable.nougat, 5000, 16000),
        Dessert(R.drawable.oreo, 6000, 20000)
    )
    private var currentDessert=allDesserts[0]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity","onCreated called")
        dessertsSold=0
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }
        desssertTimer= DesssertTimer(this.lifecycle)
        //saaving the data from the activity after the app is inactive
        if (savedInstanceState !=null){
            revenue=savedInstanceState.getInt(KEY_REVENUE)
            dessertsSold=savedInstanceState.getInt(KEY_DESSERTS_SOLD)
        }
        // Set the TextViews to the right values
        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Make sure the correct dessert is showing
        binding.dessertButton.setImageResource(currentDessert.imageId)


    }

    private fun onDessertClicked() {
        // Update the score
        revenue += currentDessert.price
        dessertsSold++

        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // Show the next dessert
        showCurrentDessert()
    }

    private fun showCurrentDessert() {
        var newDessert = allDesserts[0]
        for (dessert in allDesserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                newDessert = dessert
            }
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            else break
        }

        // If the new dessert is actually different than the current dessert, update the image
        if (newDessert != currentDessert) {
            currentDessert = newDessert
            binding.dessertButton.setImageResource(newDessert.imageId)
        }
    }
    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(getString(R.string.share_text, dessertsSold, revenue))
            .setType("text/plain")
            .intent
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.sharing_not_available),
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareMenuButton -> onShare()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_REVENUE,revenue)
        outState.putInt("key_dessertsSold",dessertsSold)

        Timber.i("onSavedInstance is called")
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity","onCreated called")
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
//        desssertTimer.startTimer()
//        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
//      desssertTimer.stopTimer()
        Timber.i("onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroyed called")
    }
}
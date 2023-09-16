package ru.artemaccount.sportclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import ru.artemaccount.sportclub.adapter.PersonsAdapter
import ru.artemaccount.sportclub.data.Person
import ru.artemaccount.sportclub.databinding.ActivityMainBinding
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.floatingButton.setOnClickListener {
            val intent = Intent(this, AddPersonActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        showData()
    }

    private fun showData() {
        CoroutineScope(EmptyCoroutineContext).launch {
            var adapter: PersonsAdapter
            withContext(Dispatchers.Main) {
                val dataset = SportClubApplication.db.getAll()
                adapter = PersonsAdapter(dataset, this@MainActivity)
                binding.recyclerList.setHasFixedSize(true)
                val manager = LinearLayoutManager(this@MainActivity)
                binding.recyclerList.adapter = adapter
                binding.recyclerList.layoutManager = manager
            }
        }
    }
}
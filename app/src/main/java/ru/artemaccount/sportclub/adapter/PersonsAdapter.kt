package ru.artemaccount.sportclub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.artemaccount.sportclub.AddPersonActivity
import ru.artemaccount.sportclub.MainActivity
import ru.artemaccount.sportclub.R
import ru.artemaccount.sportclub.SportClubApplication
import ru.artemaccount.sportclub.data.Person
import kotlin.coroutines.EmptyCoroutineContext

class PersonsAdapter(private val dataSet: List<Person>, private val context: Context) :
    RecyclerView.Adapter<PersonsAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val person = dataSet[adapterPosition]
                val intent = Intent(it.context, AddPersonActivity::class.java)
                intent.putExtra("id", person.id)
                intent.putExtra("firstname", person.firstName)
                intent.putExtra("lastname", person.lastName)
                intent.putExtra("gender", person.gender)
                intent.putExtra("group", person.group)
                context.startActivity(intent)
            }
        }

        var id: TextView = itemView.findViewById(R.id.id_item)
        var firstName: TextView = itemView.findViewById(R.id.first_name_item)
        var lastName: TextView = itemView.findViewById(R.id.last_name_item)
        var group: TextView = itemView.findViewById(R.id.group_item)
        var gender: TextView = itemView.findViewById(R.id.gender_item)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.new_person_item, viewGroup, false)

        return PersonViewHolder(view)
    }


    override fun onBindViewHolder(personViewHolder: PersonViewHolder, position: Int) {
        val item = dataSet[position]
        personViewHolder.id.text = item.id.toString()
        personViewHolder.firstName.text = item.firstName
        personViewHolder.lastName.text = item.lastName
        personViewHolder.group.text = item.group
        personViewHolder.gender.text = item.gender
    }

    override fun getItemCount() = dataSet.size

}
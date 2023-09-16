package ru.artemaccount.sportclub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import ru.artemaccount.sportclub.data.Person
import ru.artemaccount.sportclub.databinding.ActivityAddPersonBinding
import kotlin.coroutines.EmptyCoroutineContext

class AddPersonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPersonBinding
    private lateinit var spinnerAdapter: ArrayAdapter<CharSequence>
    private var personId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSpinner()

        personId = intent.getIntExtra("id", -1)

        if (personId == -1) {
            title = "Add person"
            invalidateOptionsMenu()
        } else {
            title = "Edit person"
            fillValues()
        }

    }

    private fun initSpinner() {
        spinnerAdapter =
            ArrayAdapter.createFromResource(
                this,
                R.array.array_gender,
                android.R.layout.simple_spinner_item
            )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = spinnerAdapter
    }

    private fun fillValues() {
        val firstname = intent.getStringExtra("firstname")
        val lastname = intent.getStringExtra("lastname")

        val gender = intent.getStringExtra("gender")
        val group = intent.getStringExtra("group")
        val person = Person(personId, firstname, lastname, gender, group)

        val genderPosition = spinnerAdapter.getPosition(gender)
        binding.firstNameEt.setText(person.firstName)
        binding.lastNameEt.setText(person.lastName)
        binding.groupEt.setText(person.group)
        binding.genderSpinner.setSelection(genderPosition)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_member_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        if (personId == -1) {
            menu?.findItem(R.id.delete_person)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_person -> {
                if (personId == -1) {
                    checkAndAddPerson()
                } else {
                    editPerson()
                }
                true
            }
            R.id.delete_person -> {
                showDeleteDialog()
                true
            }

            else -> true
        }
    }

    private fun showDeleteDialog() {
        val firstName = binding.firstNameEt.text.toString()
        val lastName = binding.lastNameEt.text.toString()
        val builder = AlertDialog.Builder(this@AddPersonActivity)
        builder.setMessage("Delete $firstName $lastName?")
            .setCancelable(false)
            .setPositiveButton("Delete") { _, _ ->
                deletePersonById()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog?.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun editPerson() {
        val person = Person(
            personId,
            firstName = binding.firstNameEt.text.toString(),
            lastName = binding.lastNameEt.text.toString(),
            group = binding.groupEt.text.toString(),
            gender = binding.genderSpinner.selectedItem.toString()
        )

        CoroutineScope(EmptyCoroutineContext).launch {
            SportClubApplication.db.update(person)
        }

        Toast.makeText(this@AddPersonActivity, "Person updated!", Toast.LENGTH_SHORT)
            .show()
        val intent = Intent(this@AddPersonActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun deletePersonById() {
        if (personId != -1) {
            CoroutineScope(EmptyCoroutineContext).launch {
                SportClubApplication.db.deletePersonById(personId)
            }
            Toast.makeText(this@AddPersonActivity, "Person deleted!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@AddPersonActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkAndAddPerson() {
        with(binding) {
            if (isNullOrBlank(firstNameEt, lastNameEt, groupEt, genderSpinner)) {
                return@checkAndAddPerson
            } else {
                val person = Person(
                    null,
                    firstName = firstNameEt.text.toString(),
                    lastName = lastNameEt.text.toString(),
                    group = groupEt.text.toString(),
                    gender = genderSpinner.selectedItem.toString()
                )
                CoroutineScope(EmptyCoroutineContext).launch {
                    SportClubApplication.db.insert(person)
                }
                Log.d("myTagBro", person.toString())
                Toast.makeText(
                    this@AddPersonActivity,
                    "Person added!",
                    Toast.LENGTH_SHORT
                )
                    .show()
                val intent = Intent(this@AddPersonActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isNullOrBlank(vararg views: View): Boolean {
        var isNullOrBlank = false
        views.asSequence()
            .forEach { view ->
                if (view is EditText) {
                    if (view.text.isNullOrBlank()) {
                        view.error = "Please, enter value"
                        isNullOrBlank = true
                    }
                } else if (view is Spinner) {
                    if (view.selectedItem.toString() == "Unknown") {
                        (view.selectedView as TextView).error = "Please, select value"
                        isNullOrBlank = true
                    }
                }
            }
        return isNullOrBlank
    }

}
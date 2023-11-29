package com.example.movies.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.movies.Adapter.CurrentMovie
import com.example.movies.Model.Movie
import com.example.movies.R
import com.example.movies.databinding.FragmentAddMovieBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddMovieFragment : Fragment() {
    private var db = Firebase.firestore
    private var _binding:FragmentAddMovieBinding? = null

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val binding get() = _binding!!
    private val movie: Movie? = CurrentMovie.movie

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAddMovieBinding.inflate(inflater, container, false)
        val view = binding.root
        getFields()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movie?.let { m ->
            fillContactFields(m)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearFields() {
        binding.txtName.setText("")

        val calendar = Calendar.getInstance()
        val currentDate = calendar.timeInMillis
        binding.calendarView.date = currentDate

        binding.ratingBar.numStars = 0
        binding.ratingBar.rating = 0.0f
    }

    private fun fillContactFields(movie: Movie) {
        binding.txtName.setText(movie.name)

        val date = dateFormat.parse(movie.logDate)
        val timeInMillis = date.time

        binding.calendarView.date = timeInMillis

        binding.ratingBar.rating = movie.rating?.toFloat() ?: 0.0f
    }

    private fun getFields() {

        binding.btnSave.setOnClickListener {

            val name = binding.txtName.text.toString()

            val timeInMillis = binding.calendarView.date
            val logDate = dateFormat.format(Date(timeInMillis))

            val rating = binding.ratingBar.rating.toString()

            if (name.isEmpty()) {
                binding.txtName.error = getString(R.string.error_register_name)
            } else {
                val contact = hashMapOf(
                    "name" to name,
                    "logDate" to logDate,
                    "rating" to rating,
                )

                saveMovieDB(contact)
            }
        }
    }

    private fun saveMovieDB(movie: HashMap<String, String>) {

        if(this.movie != null) {
            getFields()
            val userRef = db.collection("movies").document(this.movie.id!!)
            userRef.update(movie as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(
                        activity,
                        getString(R.string.movie_edit_done),
                        Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        activity,
                        getString(R.string.movie_edit_error),
                        Toast.LENGTH_LONG).show()
                }

        } else {
            val id = UUID.randomUUID().toString()
            db.collection("movies").document(id)
                .set(movie)
                .addOnSuccessListener {
                    clearFields()
                    Toast.makeText(
                        activity,
                        getString(R.string.movie_register_done),
                        Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        activity,
                        it.toString(),
                        Toast.LENGTH_LONG).show()
                }
        }
    }
}
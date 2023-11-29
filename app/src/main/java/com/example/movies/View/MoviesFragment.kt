package com.example.movies.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.Adapter.CurrentMovie
import com.example.movies.Adapter.MovieAdapter
import com.example.movies.Model.Movie
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoviesFragment : Fragment(), MovieAdapter.OnEditClickListener, MovieAdapter.OnShareClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var moviesList: ArrayList<Movie>
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactInit()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
    }

    private fun contactInit() {
        db = FirebaseFirestore.getInstance()
        db.collection("movies")
            .addSnapshotListener { documents, _ ->
                moviesList = arrayListOf()
                documents?.map { document ->
                    val contact = Movie(
                        id = document.id,
                        name = document.getString("name"),
                        logDate = document.getString("logDate"),
                        rating = document.getString("rating"),
                    )
                    moviesList.add(contact)
                }
                recyclerView.adapter = MovieAdapter(moviesList, this, this)
            }
    }

    override fun onEditClick(movie: Movie) {
        if (CurrentMovie.movie != null) {
            val addMovieFragment = AddMovieFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, addMovieFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onShareClick(movie: Movie) {
        val textToShare = "🎬 ${movie.name} \n ⭐️ ${movie.rating} \n 📆 ${movie.logDate}"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, textToShare)

        startActivity(Intent.createChooser(intent, "Share"))
    }

}
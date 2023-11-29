package com.example.movies.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.Model.Movie
import com.example.movies.R
import com.example.movies.View.MoviesFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object CurrentMovie {
    var movie: Movie? = null
}

class MovieAdapter(
    private val moviesList: ArrayList<Movie>,
    private val onEditClickListener: OnEditClickListener,
    private val onShareClickListener: OnShareClickListener
) : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    interface OnEditClickListener {
        fun onEditClick(movie: Movie)
    }

    interface OnShareClickListener {
        fun onShareClick(movie: Movie)
    }

    private var db = Firebase.firestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentMovie = moviesList[position]
        holder.movieName.text = currentMovie.name
        holder.logDate.text = currentMovie.logDate
        holder.rating.text = currentMovie.rating

        val keyContact = currentMovie.id.toString()

        holder.btnDelete.setOnClickListener {
            db = FirebaseFirestore.getInstance()
            db.collection("movies").document(keyContact).delete()
        }

        holder.btnEdit.setOnClickListener {
            val currentMovieToEdit = moviesList[position]
            CurrentMovie.movie = currentMovieToEdit
            onEditClickListener.onEditClick(currentMovieToEdit)
        }

        holder.btnShare.setOnClickListener{
            val currentMovieToShare = moviesList[position]
            CurrentMovie.movie = currentMovieToShare
            onShareClickListener.onShareClick(currentMovieToShare)
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class MyViewHolder(movieView: View) : RecyclerView.ViewHolder(movieView) {
        val movieName: TextView = itemView.findViewById(R.id.tvMovieName)
        val logDate: TextView = itemView.findViewById(R.id.tvLogDate)
        val rating: TextView = itemView.findViewById(R.id.tvRating)
        var btnDelete: ImageView = itemView.findViewById(R.id.btn_delete)
        var btnEdit: ImageView = itemView.findViewById(R.id.btn_edit)
        var btnShare: ImageView = itemView.findViewById(R.id.btn_share)
    }
}
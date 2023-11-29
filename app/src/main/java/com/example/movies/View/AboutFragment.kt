package com.example.movies.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movies.R
import com.example.movies.databinding.FragmentAboutBinding
import com.example.movies.databinding.FragmentMoviesBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AboutFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private var mapView: MapView? = null

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
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        mapView = binding.mapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        binding.btnCall.setOnClickListener {
            fazerLigacao()
        }
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val fiap = LatLng(-23.5640843, -46.6523865)

        googleMap.addMarker(
            MarkerOptions()
                .position(fiap)
                .title("FIAP")
                .snippet("Sede do MBA")
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiap, 15f))
    }

    fun fazerLigacao() {
        val numeroTelefone = "1133858010"

        val intent = Intent(Intent.ACTION_DIAL)

        intent.data = Uri.parse("tel:$numeroTelefone")

        startActivity(intent)
    }
}
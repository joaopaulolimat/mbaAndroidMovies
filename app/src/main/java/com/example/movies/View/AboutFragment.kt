package com.example.movies.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movies.BuildConfig
import com.example.movies.databinding.FragmentAboutBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig

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
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

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
        registerObserver()
        return binding.root
    }

    private fun registerObserver() {
        val isFeatureEnabled = remoteConfig.getBoolean("hide_map")
        val appVersionRemote = remoteConfig.getString("app_version")

        if (isFeatureEnabled) {
            binding.mapView.visibility = GONE
        } else {
            binding.mapView.visibility = VISIBLE
        }

        if(!appVersionRemote.isNullOrEmpty()) {
            binding.textView2.text = appVersionRemote
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val fiapPaulista = LatLng(-23.5640843, -46.6523865)
        val fiapAclimacao = LatLng(-23.574083,-46.705629)

        when (BuildConfig.FLAVOR) {
            "world" -> {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(fiapAclimacao)
                        .title("FIAP Aclimação")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiapAclimacao, 15f))
            }
            "brasil" -> {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(fiapPaulista)
                        .title("FIAP Paulista")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiapPaulista, 15f))
            }
        }
    }

    fun fazerLigacao() {
        val numeroTelefone = "1133858010"

        val intent = Intent(Intent.ACTION_DIAL)

        intent.data = Uri.parse("tel:$numeroTelefone")

        startActivity(intent)
    }
}
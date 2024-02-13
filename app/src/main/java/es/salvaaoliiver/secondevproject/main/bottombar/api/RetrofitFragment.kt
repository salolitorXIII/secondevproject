package es.salvaaoliiver.secondevproject.main.bottombar.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import es.salvaaoliiver.secondevproject.databinding.FragmentRetrofitBinding
import es.salvaaoliiver.secondevproject.main.MainActivity
import es.salvaaoliiver.secondevproject.main.bottombar.api.`object`.NasaPictureOfDay
import es.salvaaoliiver.secondevproject.main.bottombar.api.room.DataStorageManager
import es.salvaaoliiver.secondevproject.main.bottombar.api.room.NasaPictureDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFragment : Fragment() {

    private lateinit var binding: FragmentRetrofitBinding
    private lateinit var adapter: NasaPicturesAdapter
    private lateinit var dao: NasaPictureDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRetrofitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, "Wait...", Toast.LENGTH_LONG).show()

        (activity as MainActivity).supportActionBar?.title = "Api"


        adapter = NasaPicturesAdapter(emptyList())

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        val apiKey = "N8vQT1m87x3GkEo6mR8nmj2WsAEcIblA2YM5LnGh"

        val offlineModeEnabled = obtenerEstadoOfflineModeDesdePreferencias()

        lifecycleScope.launch(Dispatchers.IO) {
            val response: List<NasaPictureOfDay>?
            if (offlineModeEnabled) {
                response = cargarImagenesDesdeAlmacenamientoLocal()
            } else {
                response = Retrofit.Builder()
                    .baseUrl("https://api.nasa.gov/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(NasaApiService::class.java)
                    .getPicturesOfPreviousDays(apiKey, 26)
                    .body()
            }

            withContext(Dispatchers.Main) {
                if (response != null) {
                    adapter.updateImages(response)
                    if (offlineModeEnabled) {
                        dao.deleteAll()
                        dao.insertAll(response)
                    }
                } else {
                    Toast.makeText(context, "Error cargando las imagenes", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun cargarImagenesDesdeAlmacenamientoLocal(): List<NasaPictureOfDay> {
        dao = DataStorageManager.getInstance(requireContext()).nasaPictureDao()
        return dao.getAllImages()
    }

    private fun obtenerEstadoOfflineModeDesdePreferencias(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return sharedPreferences.getBoolean("offline_mode", false)
    }
}

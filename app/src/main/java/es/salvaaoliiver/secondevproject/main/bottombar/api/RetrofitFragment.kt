package es.salvaaoliiver.secondevproject.main.bottombar.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import es.salvaaoliiver.secondevproject.databinding.FragmentRetrofitBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFragment : Fragment() {

    private lateinit var binding: FragmentRetrofitBinding
    private lateinit var adapter: NasaPicturesAdapter

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

        adapter = NasaPicturesAdapter(emptyList())

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        val apiKey = "N8vQT1m87x3GkEo6mR8nmj2WsAEcIblA2YM5LnGh"

        lifecycleScope.launch(Dispatchers.IO) {
            val call = Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NasaApiService::class.java)
                .getPicturesOfPreviousDays(apiKey, 26)
            val response = call.body()
            withContext(Dispatchers.Main) {
                if (response != null) {
                    adapter.updateImages(response)
                } else {
                    Toast.makeText(requireContext(), "Error fetching images", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}

package es.salvaaoliiver.secondevproject.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationBarView
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.ActivityMainBinding
import es.salvaaoliiver.secondevproject.login.AuthManager
import es.salvaaoliiver.secondevproject.main.add.AddFragment
import es.salvaaoliiver.secondevproject.main.api.RetrofitFragment
import es.salvaaoliiver.secondevproject.main.chat.ChatFragment
import es.salvaaoliiver.secondevproject.main.database.RecipesRepository
import es.salvaaoliiver.secondevproject.main.home.HomeFragment
import es.salvaaoliiver.secondevproject.main.search.SearchFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() , NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.myToolbar)
        binding.bottomNavigation.setOnItemSelectedListener(this)

        val view = binding.root

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val idUser = RecipesRepository.getUserIdByEmail(AuthManager.getCorreo())
                if (idUser != null) {
                    RecipesRepository.setUserId(idUser)
                }
                withContext(Dispatchers.Main) {
                    setContentView(view)
                    homeFragment = HomeFragment()
                    loadFragment(homeFragment)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error initializing userID", e)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu ->{
                true
            }
            R.id.multimedia -> {
                true
            } else -> false
        }
    }

    // REPLACE FRAGMENT
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnHome -> {
                loadFragment(HomeFragment())
                return true
            }
            R.id.btnSearch -> {
                loadFragment(SearchFragment())
                return true
            }
            R.id.btnAdd -> {
                loadFragment(AddFragment())
                return true
            }
            R.id.btnChat -> {
                loadFragment(ChatFragment())
                return true
            }
            R.id.btnAPI -> {
                loadFragment(RetrofitFragment())
                return true
            }
        }
        return false
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFragmentoContainer, fragment)
            .commit()
    }
    // END REPLACE FRAGMENT

}
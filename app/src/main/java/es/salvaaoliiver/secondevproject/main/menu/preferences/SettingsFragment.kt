package es.salvaaoliiver.secondevproject.main.menu.preferences

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.main.bottombar.home.HomeFragment

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.menuFragmentoContainer, HomeFragment())
                .commit()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }


}
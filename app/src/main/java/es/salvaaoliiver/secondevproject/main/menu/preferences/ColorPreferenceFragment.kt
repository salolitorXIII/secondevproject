package es.salvaaoliiver.secondevproject.main.menu.preferences

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.main.MainActivity
import es.salvaaoliiver.secondevproject.main.bottombar.home.HomeFragment


class ColorPreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.menuFragmentoContainer, SettingsFragment())
                .commit()
        }

        (activity as MainActivity).supportActionBar?.title = "Settings"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.color_preference, rootKey)

        val fontColorPreference = findPreference<Preference>("chat_font_color")
        val bgColorPreference = findPreference<Preference>("chat_bg_color")

        fontColorPreference?.setOnPreferenceClickListener {
            showColorPicker(requireContext(), "chat_font_color")
            true
        }

        bgColorPreference?.setOnPreferenceClickListener {
            showColorPicker(requireContext(), "chat_bg_color")
            true
        }
    }



    private fun showColorPicker(context: Context, preferenceKey: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        ColorPickerDialogBuilder
            .with(context)
            .setTitle("Choose color")
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { selectedColor ->
                Toast.makeText(context, "seleccionado", Toast.LENGTH_SHORT).show()
            }
            .setPositiveButton("ok") { dialog, selectedColor, allColors ->
                sharedPreferences.edit().putInt(preferenceKey, selectedColor).apply()
            }
            .setNegativeButton("cancel") { dialog, which -> }
            .build()
            .show()
    }

}

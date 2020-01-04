package io.github.skywalkerdarren.simpleaccounting.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import io.github.skywalkerdarren.simpleaccounting.R
import io.github.skywalkerdarren.simpleaccounting.util.NotificationWorker

class SettingsFragment : PreferenceFragmentCompat() {
    companion object {
        fun newInstance() = SettingsFragment()
        private const val TAG = "SettingsFragment"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val notificationSwitch: SwitchPreferenceCompat? = findPreference("notifications")
        notificationSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                NotificationWorker.start(requireContext())
            } else {
                NotificationWorker.cancel(requireContext())
            }
            true
        }
    }
}
package com.example.spinnerdialog

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.spinnerdialog.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val presensi = listOf(
            "Hadir Tepat Waktu",
            "Sakit",
            "Terlambat",
            "Izin"
        )

        val date = Calendar.getInstance()
        var selectedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date.time)
        var selectedTime = amPm (date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE))
        val adapterPresensi = ArrayAdapter(this@MainActivity, R.layout.spinner_style, presensi)

        with(binding){
            spinner.adapter = adapterPresensi
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedValue = presensi[position]

                    if (selectedValue == presensi[0]) {
                        binding.keterangan.visibility = View.INVISIBLE
                    }
                    else {
                        binding.keterangan.visibility = View.VISIBLE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

            binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, month)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(date.time)
            }

            binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                selectedTime = amPm(hourOfDay, minute)
            }

            binding.btnSubmit.setOnClickListener {
                val selectedPresensi = spinner.selectedItem.toString()
                val keteranganText = keterangan.text.toString()
                val message = "Presensi berhasil $selectedDate jam $selectedTime"

                if (selectedPresensi in presensi.subList(1, 4)) {
                    if (keteranganText.isEmpty()) {
                        showSnackbar("Mohon isi keterangan")
                    } else {
                        showSnackbar(message)
                    }
                } else {
                    showSnackbar(message)
                }
            }
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextAppearance(R.style.SnackbarText) // Menggunakan gaya teks yang telah ditentukan
        snackbar.show()
    }

    private fun amPm (hourOfDay:Int, minute:Int): String {
        var selectedTime = ""
        var minutes = minute.toString()
        if (minute < 10) {
            minutes = "0$minute"
        }
        if (hourOfDay < 12) {
            selectedTime = "$hourOfDay:$minutes AM"
        }
        else if (hourOfDay > 12) {
            selectedTime = "${hourOfDay % 12}:$minutes PM"
        }
        else {
            if (minute > 0) {
                selectedTime = "${hourOfDay % 12}:$minutes PM"
            }
            else if (minute == 0) {
                selectedTime = "$hourOfDay:$minutes AM"
            }
        }
        return selectedTime
    }
}
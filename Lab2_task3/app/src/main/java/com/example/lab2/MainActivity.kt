package com.example.lab2

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editName = findViewById<EditText>(R.id.editName)
        val editDob = findViewById<EditText>(R.id.editAge)
        val btnProfile = findViewById<Button>(R.id.btnNextScreen)
        val btnWeb = findViewById<Button>(R.id.btnOpenWeb)
        val btnTxtSharing = findViewById<Button>(R.id.btnShare)

        // Show DatePickerDialog when clicking DOB field
        editDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(
                        Locale.US,
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                    editDob.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        btnProfile.setOnClickListener {
            val name = editName.text.toString().trim()
            val dob = editDob.text.toString().trim()

            if (name.isNotEmpty() && dob.isNotEmpty()) {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("NAME", name)
                intent.putExtra("DOB", dob)
                intent.putExtra("AGE", dob)
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT).show()
            }
        }

        btnWeb.setOnClickListener {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            startActivity(webIntent)
        }

        btnTxtSharing.setOnClickListener {
            val name = editName.text.toString().trim()
            val dob = editDob.text.toString().trim()
            val textShared = "Nama: $name, Tarikh Lahir: $dob"
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, textShared)
            }
            startActivity(Intent.createChooser(shareIntent, "Kongsi menggunakan..."))
        }
    }
}
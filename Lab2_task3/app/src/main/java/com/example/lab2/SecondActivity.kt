package com.example.lab2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtName = findViewById<TextView>(R.id.tvDisplayName)
        val txtCharCount = findViewById<TextView>(R.id.tvCharCount)
        val txtVowelCount = findViewById<TextView>(R.id.tvVowelCount)
        val txtRepeatedChars = findViewById<TextView>(R.id.tvRepeatedChars)
        val txtAge = findViewById<TextView>(R.id.tvDisplayAge)
        val txtDayOfBirth = findViewById<TextView>(R.id.tvDayOfBirth)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val receivedName = intent.getStringExtra("NAME") ?: "Ahmad bin Ali"
        val receivedDob = intent.getStringExtra("DOB") ?: intent.getStringExtra("AGE") ?: "2000-05-15"

        // --- 1. PART A: NAME ANALYSIS ---
        // a. Total number of characters excluding spaces
        val nameNoSpace = receivedName.replace(" ", "")
        val charCount = nameNoSpace.length

        // b. Number of vowel letters
        val vowelCount = nameNoSpace.count { it.lowercaseChar() in "aeiou" }

        // c. List of repeated characters
        val repeatedList = nameNoSpace.groupBy { it.lowercaseChar() }
            .filter { it.value.size > 1 }
            .keys.sorted()
            .joinToString(", ")

        val repeatedText = if (repeatedList.isNotEmpty()) repeatedList else "Tiada"

        txtName.text = getString(R.string.label_name, receivedName)
        txtCharCount.text = getString(R.string.label_char_count, charCount)
        txtVowelCount.text = getString(R.string.label_vowel_count, vowelCount)
        txtRepeatedChars.text = getString(R.string.label_repeated_chars, repeatedText)

        // --- 2. PART B: DATE OF BIRTH ANALYSIS ---
        val (age, dayOfWeek) = calculateAgeAndDay(receivedDob)

        txtAge.text = getString(R.string.label_age, age)
        txtDayOfBirth.text = getString(R.string.label_day_of_birth, dayOfWeek)

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun calculateAgeAndDay(dobString: String): Pair<Int, String> {
        val formats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd-MM-yyyy", "dd/MM/yyyy")
        var parsedDate: Date? = null

        for (format in formats) {
            try {
                val sdf = SimpleDateFormat(format, Locale.US)
                sdf.isLenient = false
                parsedDate = sdf.parse(dobString)
                if (parsedDate != null) break
            } catch (_: Exception) {
                // Try next format
            }
        }

        if (parsedDate == null) {
            // Fallback if user entered an age integer instead of date string
            val ageInt = dobString.toIntOrNull()
            return if (ageInt != null) {
                Pair(ageInt, "Tiada data tarikh")
            } else {
                Pair(0, "Format tarikh tidak sah")
            }
        }

        val dobCal = Calendar.getInstance().apply { time = parsedDate }
        val todayCal = Calendar.getInstance()

        var age = todayCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR)
        if (todayCal.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        val dayNames = mapOf(
            Calendar.SUNDAY to "Ahad",
            Calendar.MONDAY to "Isnin",
            Calendar.TUESDAY to "Selasa",
            Calendar.WEDNESDAY to "Rabu",
            Calendar.THURSDAY to "Khamis",
            Calendar.FRIDAY to "Jumaat",
            Calendar.SATURDAY to "Sabtu"
        )

        val dayOfWeekInt = dobCal.get(Calendar.DAY_OF_WEEK)
        val dayName = dayNames[dayOfWeekInt] ?: "Bukan hari sah"

        return Pair(age, dayName)
    }
}
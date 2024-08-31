package br.edu.ifsp.dmo.diario.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo.diario.databinding.ActivityAddDiaryEntryBinding
import br.edu.ifsp.dmo.diario.model.DiaryEntry
import br.edu.ifsp.dmo.diario.viewModel.DiaryViewModel
import java.util.Calendar
import java.util.Date

class AddDiaryEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDiaryEntryBinding
    private lateinit var diaryViewModel: DiaryViewModel
    private var selectedDate: Date? = null
    private var diaryEntry: DiaryEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDiaryEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        // Recupera o objeto DiaryEntry se ele foi passado
        if (intent.hasExtra("diaryEntry")) {
            diaryEntry = intent.getSerializableExtra("diaryEntry") as DiaryEntry
            populateFields(diaryEntry)
        }

        setupDateAndTimePickers()
        setupSaveButton()
    }



    private fun populateFields(entry: DiaryEntry?) {
        entry?.let {
            binding.editTextTitle.setText(it.title)
            binding.editTextContent.setText(it.content)
            binding.editTextLocation.setText(it.location)
            selectedDate = it.date
            binding.buttonSelectDate.text = it.date.toString()  // Formate a data conforme necessário
            // Se precisar, formate a hora também
        }
    }

    private fun setupDateAndTimePickers() {
        binding.buttonSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                binding.buttonSelectDate.text = "$dayOfMonth/${month + 1}/$year"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePicker.show()
        }

        binding.buttonSelectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                selectedDate?.let {
                    it.time = calendar.timeInMillis
                }
                binding.buttonSelectTime.text = "$hourOfDay:$minute"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

            timePicker.show()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            saveOrUpdateDiaryEntry()
        }
    }

    private fun saveOrUpdateDiaryEntry() {
        val title = binding.editTextTitle.text.toString()
        val content = binding.editTextContent.text.toString()
        val location = binding.editTextLocation.text.toString()
        val date = selectedDate ?: Calendar.getInstance().time

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val entry = diaryEntry?.copy(
                title = title,
                content = content,
                location = location,
                date = date
            ) ?: DiaryEntry(
                title = title,
                content = content,
                location = location,
                date = date
            )

            if (diaryEntry != null) {
                diaryViewModel.update(entry)
            } else {
                diaryViewModel.insert(entry)
            }

            finish()
        } else {
            Toast.makeText(this, "Título e conteúdo são obrigatórios", Toast.LENGTH_SHORT).show()
        }
    }
}

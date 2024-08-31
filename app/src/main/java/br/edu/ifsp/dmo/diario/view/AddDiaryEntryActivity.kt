package br.edu.ifsp.dmo.diario.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDiaryEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        // Configura seleção de data
        binding.buttonSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                binding.buttonSelectDate.text = "$dayOfMonth/${month + 1}/$year"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePicker.show()
        }

        // Configura seleção de hora
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

        // Configura o botão de salvar
        binding.saveButton.setOnClickListener {
            saveDiaryEntry()
        }
    }

    private fun saveDiaryEntry() {
        // Obtém os valores dos campos de texto
        val title = binding.editTextTitle.text.toString()
        val content = binding.editTextContent.text.toString()
        val location = binding.editTextLocation.text.toString()
        val date = selectedDate ?: Calendar.getInstance().time // Use a data atual se nenhuma data for selecionada

        // Verifica se o título e conteúdo não estão vazios
        if (title.isNotEmpty() && content.isNotEmpty()) {
            // Cria uma nova entrada do diário
            val diaryEntry = DiaryEntry(
                title = title,
                content = content,
                location = location,
                date = date
            )

            // Salva a entrada no banco de dados
            diaryViewModel.insert(diaryEntry)

            // Fecha a Activity após salvar
            finish()
        } else {
            // Se necessário, adicione uma mensagem para informar ao usuário que os campos são obrigatórios
            // Exemplo: Toast.makeText(this, "Título e conteúdo são obrigatórios", Toast.LENGTH_SHORT).show()
        }
    }
}

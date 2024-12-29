package com.gabrielmedrado.pettimefinalplus.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmedrado.pettimefinalplus.R
import com.gabrielmedrado.pettimefinalplus.databinding.ActivityAgendamentoBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class Agendamento : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentoBinding
    private val calendar: Calendar = Calendar.getInstance()
    private var data: String = ""
    private var hora: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val nome = intent.extras?.getString("nome").toString()

        val datePicker = binding.datePicker
        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var dia = dayOfMonth.toString()
            val mes: String

            if (dayOfMonth < 10) {
                dia = "0$dayOfMonth"
            }
            mes = if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else (monthOfYear + 1).toString()

            data = "$dia/$mes/$year"
        }

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val minuto = if (minute < 10) "0$minute" else minute.toString()
            hora = "$hourOfDay:$minuto"
        }
        binding.timePicker.setIs24HourView(true)

        binding.btAgendar.setOnClickListener {
            val banho = binding.banho
            val tosa = binding.tosa
            val hidratacao = binding.hidratacao
            val vacina = binding.vacina

            // Obtém hora e data
            val horaSelecionada = binding.timePicker.hour.toString().padStart(2, '0') + ":" + binding.timePicker.minute.toString().padStart(2, '0')
            val dataSelecionada = "${binding.datePicker.dayOfMonth}/${binding.datePicker.month + 1}/${binding.datePicker.year}"

            when {
                horaSelecionada.isEmpty() -> {
                    mensagem(it, "Preencha o horário!", "#FF0000")
                    return@setOnClickListener
                }
                horaSelecionada < "08:00" || horaSelecionada > "19:00" -> {
                    mensagem(it, "Estabelecimento fechado - horário de atendimento das 08:00 às 19:00!", "#FF0000")
                    return@setOnClickListener
                }
                dataSelecionada.isEmpty() -> {
                    mensagem(it, "Coloque uma data!", "#FF0000")
                    return@setOnClickListener
                }
            }

            val servicoAgendado = when {
                banho.isChecked -> "Banho"
                tosa.isChecked -> "Tosa"
                hidratacao.isChecked -> "Hidratação"
                vacina.isChecked -> "Vacina"
                else -> null
            }

            val profissional1 = binding.profissional1
            val profissional2 = binding.profissional2
            val profissional3 = binding.profissional3

            val profissionalSelecionado = when {
                profissional1.isChecked -> "Pedro Pinheiros"
                profissional2.isChecked -> "Gustavo Ribeiro"
                profissional3.isChecked -> "Gabriel Negri"
                else -> null
            }

            if (servicoAgendado != null && profissionalSelecionado != null) {
                mensagem(it, "Agendamento realizado com sucesso! Serviço: $servicoAgendado com $profissionalSelecionado", "#FF03DAC5")
            } else {
                mensagem(it, "Escolha um serviço e um profissional!", "#FF0000")
            }
        }

        binding.profissional1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.profissional2.isChecked = false
                binding.profissional3.isChecked = false
            }
        }

        binding.profissional2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.profissional1.isChecked = false
                binding.profissional3.isChecked = false
            }
        }

        binding.profissional3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.profissional1.isChecked = false
                binding.profissional2.isChecked = false
            }
        }
    }

    private fun mensagem(view: View, mensagem: String, cor: String) {
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }
}

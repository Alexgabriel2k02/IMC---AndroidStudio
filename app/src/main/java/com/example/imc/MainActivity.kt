package com.example.app_imc

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.imc.ImcDao
import com.example.imc.ImcDatabase
import com.example.imc.ImcEntity
import com.example.imc.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etAltura: EditText
    private lateinit var etPeso: EditText
    private lateinit var rgGenero: RadioGroup
    private lateinit var rbMasculino: RadioButton
    private lateinit var rbFeminino: RadioButton
    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView
    private lateinit var database: ImcDatabase
    private lateinit var imcDao: ImcDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializando as views
        etAltura = findViewById(R.id.etAltura)
        etPeso = findViewById(R.id.etPeso)
        rgGenero = findViewById(R.id.rgGenero)
        rbMasculino = findViewById(R.id.rbMasculino)
        rbFeminino = findViewById(R.id.rbFeminino)
        btnCalcular = findViewById(R.id.button2)
        tvResultado = findViewById(R.id.tvResultado)
        database = ImcDatabase.getDatabase(this)
        imcDao = database.imcDao()

        // Ação do botão calcular
        btnCalcular.setOnClickListener {
            calcularIMC()
        }

        // Exibir todos os IMCs armazenados
        lerImcs()
    }

    private fun calcularIMC() {
        val alturaStr = etAltura.text.toString()
        val pesoStr = etPeso.text.toString()

        if (alturaStr.isNotEmpty() && pesoStr.isNotEmpty()) {
            val altura = alturaStr.toDoubleOrNull()
            val peso = pesoStr.toDoubleOrNull()

            if (altura != null && peso != null) {
                val imc = peso / (altura * altura)

                val genero = when (rgGenero.checkedRadioButtonId) {
                    R.id.rbMasculino -> "Masculino"
                    R.id.rbFeminino -> "Feminino"
                    else -> "Não especificado"
                }

                // Exibir resultado com base no gênero
                tvResultado.text = "Seu IMC é: %.2f\nGênero: %s".format(imc, genero)

                // Inserir o IMC no banco de dados
                val imcEntity = ImcEntity(altura = altura, peso = peso, imc = imc, genero = genero)
                CoroutineScope(Dispatchers.IO).launch {
                    imcDao.insert(imcEntity)
                    lerImcs() // Atualizar a lista após a inserção
                }

            } else {
                Toast.makeText(this, "Insira valores numéricos válidos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun lerImcs() {
        CoroutineScope(Dispatchers.IO).launch {
            val imcList = imcDao.getAllImc()
            runOnUiThread {
                // Atualize a UI com os dados de imcList
                tvResultado.text = imcList.joinToString("\n") { "IMC: ${it.imc}, Gênero: ${it.genero}" }
            }
        }
    }

    private fun atualizarImc(imcEntity: ImcEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            imcDao.update(imcEntity)
            lerImcs() // Atualizar a lista após a atualização
        }
    }

    private fun deletarImc(imcEntity: ImcEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            imcDao.delete(imcEntity)
            lerImcs() // Atualizar a lista após a exclusão
        }
    }
}

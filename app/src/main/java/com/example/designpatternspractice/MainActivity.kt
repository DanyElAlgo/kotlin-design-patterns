package com.example.designpatternspractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.designpatternspractice.ui.theme.DesignPatternsPracticeTheme
import com.example.designpatternspractice.DelicatePhTag
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesignPatternsPracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlowerForm(modifier = Modifier.padding(innerPadding).padding(16.dp))
                }
            }
        }
    }
}

interface Flower {
    fun name(): String
    fun description(): String
    fun region(): String
}

class FlowerBase(private val nombreValue: String, private val descValue: String, private val regValue: String) : Flower {
    override fun name(): String = nombreValue
    override fun description(): String = descValue
    override fun region(): String = regValue
}

abstract class TagDecorator(protected val flower: Flower) : Flower {
    override fun name(): String = flower.name()
    override fun description(): String = flower.description()
    override fun region(): String = flower.region()
}

class DelicatePhTag(flower: Flower, protected val ph: Double) : TagDecorator(flower) {
    override fun description(): String {
        return flower.description() + "\nVIGILA EL PH!\nEsta flor debe mantenerse siempre a un ph alrededor de $ph"
    }
}

class DelicateTempTag(flower: Flower, protected val temp: Double) : TagDecorator(flower) {
    override fun description(): String {
        return flower.description() + "\nVIGILA LA TEMPERATURA!\nEsta flor debe mantenerse siempre a una temperatura alrededor de $temp"
    }
}

// class DelicateSeasonTag(flower: Flower, protected val season: Double) : TagDecorator(flower) {
//     override fun description(): String {
//         return flower.description() + "\nEsta flor no florece todo el año!\nPertenece a la estación de $season"
//     }
// }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowerForm(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }

    var enablePH by remember { mutableStateOf(false) }
    var PH by remember { mutableStateOf("") }

    var enableTemp by remember { mutableStateOf(false) }
    var Temp by remember { mutableStateOf("") }

    var submitted by remember { mutableStateOf(false) }

    var flower: Flower = FlowerBase("Desconocido", "Sin información", "Desconocido")

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Text(text = "Formulario de Flor")

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.height(120.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = region,
            onValueChange = { region = it },
            label = { Text("Region") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = enablePH, onCheckedChange = { enablePH = it })
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Requiere HP específico")
        }

        Spacer(modifier = Modifier.height(4.dp))

        TextField(
            value = PH,
            onValueChange = { PH = it },
            label = { Text("HP sugerida") },
            enabled = enablePH
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = enableTemp, onCheckedChange = { enableTemp = it })
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Requiere temperatura específica")
        }

        Spacer(modifier = Modifier.height(4.dp))

        TextField(
            value = Temp,
            onValueChange = { Temp = it },
            label = { Text("Temperatura sugerida") },
            enabled = enableTemp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { submitted = true }) {
            Text("Enviar")
        }

        if (submitted) {

            // Sobreescribir la flor con los datos recolectados
            flower = FlowerBase(name, description, region)
            // Y ahora verificar los datos adicionales para el decorator
            if(enablePH){
                val phValue = PH.toDoubleOrNull() ?: 0.0
                flower = DelicatePhTag(flower, phValue)
            }
            if(enableTemp){
                val tempValue = Temp.toDoubleOrNull() ?: 0.0
                flower = DelicateTempTag(flower, tempValue)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Resumen:")
            Text(text = "Nombre: ${flower.name()}")
            Text(text = "Descripción: ${flower.description()}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlowerFormPreview() {
    DesignPatternsPracticeTheme {
        FlowerForm(modifier = Modifier.padding(16.dp))
    }
}
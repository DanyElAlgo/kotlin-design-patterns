package com.example.designpatternspractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.designpatternspractice.ui.theme.DesignPatternsPracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesignPatternsPracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
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

class DelicateSeasonTag(flower: Flower, protected val season: Double) : TagDecorator(flower) {
    override fun description(): String {
        return flower.description() + "\nEsta flor no florece todo el año!\nPertenece a la estación de $season"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DesignPatternsPracticeTheme {
        Greeting("Android")
    }
}
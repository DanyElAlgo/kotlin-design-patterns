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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.example.designpatternspractice.ui.theme.DesignPatternsPracticeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesignPatternsPracticeTheme {
                val newsletter = remember { Newsletter() }
                val alice = remember { User("Alice") } // siempre suscrita
                val you = remember { User("You") } // usuario manejable desde UI

                // Asegurar que Alice esté suscrita solo una vez
                DisposableEffect(Unit) {
                    newsletter.follow(alice)
                    onDispose {
                        newsletter.unfollow(alice)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ObserverDemoScreen(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        newsletter = newsletter,
                        alice = alice,
                        you = you
                    )
                }
            }
        }
    }
}

interface Follower {
    fun update(news: String)
}

class Newsletter {
    private val followers = mutableListOf<Follower>()
    private var lastNews: String = ""

    fun follow(follower: Follower) {
        if (!followers.contains(follower)) followers.add(follower)
    }

    fun unfollow(follower: Follower) {
        followers.remove(follower)
    }

    private fun notifyFollowers() {
        followers.forEach { it.update(lastNews) }
    }

    fun postNews(news: String) {
        this.lastNews = "BREAKING NEWS! $news"
        notifyFollowers()
    }
}

class User(val name: String) : Follower {
    var lastNews by mutableStateOf("")
        private set

    override fun update(news: String) {
        lastNews = news
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObserverDemoScreen(
    modifier: Modifier = Modifier,
    newsletter: Newsletter,
    alice: User,
    you: User
) {
    var newsText by remember { mutableStateOf("") }
    var youSubscribed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Entrada de noticia y botón enviar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newsText,
                onValueChange = { newsText = it },
                modifier = Modifier.weight(1f),
                label = { Text("Escribe una noticia") }
            )
            Button(onClick = {
                if (newsText.isNotBlank()) {
                    newsletter.postNews(newsText.trim())
                    newsText = ""
                }
            }) {
                Text("Enviar")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                youSubscribed = !youSubscribed
                if (youSubscribed) newsletter.follow(you) else newsletter.unfollow(you)
            }) {
                Text(if (youSubscribed) "Desuscribirse" else "Suscribirse")
            }
            Text(text = "Estado: ${if (youSubscribed) "Suscrito" else "No suscrito"}", modifier = Modifier.padding(top = 12.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bloques de usuario
        UserBlock(user = alice, title = "Usuario fijo (siempre suscrito)")
        UserBlock(user = you, title = "Nuestro usuario (puede suscribirse)")
    }
}

@Composable
fun UserBlock(user: User, title: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Nombre: ${user.name}")
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = user.lastNews.ifEmpty { "Sin noticias" })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DesignPatternsPracticeTheme {
        val alice = User("Alice")
        val you = User("You")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)) {
            UserBlock(user = alice, title = "Usuario fijo (siempre suscrito)")
            UserBlock(user = you, title = "Nuestro usuario (puede suscribirse)")
        }
    }
}
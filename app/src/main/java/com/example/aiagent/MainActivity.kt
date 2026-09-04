package com.example.aiagent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var messagesAdapter: ChatMessagesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputField: EditText
    private lateinit var sendButton: Button
    private lateinit var progressBar: ProgressBar

    // ⚠️ WICHTIG: IP-Adresse ändern zu deiner n8n-Instanz!
    private val N8N_BASE_URL = "http://192.168.1.100:5678/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI-Elemente initialisieren
        recyclerView = findViewById(R.id.messagesRecyclerView)
        inputField = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        progressBar = findViewById(R.id.progressBar)

        // Adapter und Layout Manager
        messagesAdapter = ChatMessagesAdapter()
        recyclerView.adapter = messagesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        // ViewModel erstellen
        val repository = ChatRepository.create(N8N_BASE_URL)
        viewModel = ViewModelProvider(
            this,
            ChatViewModelFactory(repository)
        ).get(ChatViewModel::class.java)

        // Observer für Messages
        viewModel.messages.observe(this) { messages ->
            messagesAdapter.submitList(messages)
            if (messages.isNotEmpty()) {
                recyclerView.scrollToPosition(messages.size - 1)
            }
        }

        // Observer für Loading
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
            sendButton.isEnabled = !isLoading
        }

        // Observer für Fehler
        viewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, "❌ $error", Toast.LENGTH_SHORT).show()
            }
        }

        // Send-Button Click
        sendButton.setOnClickListener {
            val message = inputField.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                inputField.text.clear()
            }
        }

        // Enter-Taste für Senden
        inputField.setOnKeyListener { _, keyCode, event ->
            if (keyCode == android.view.KeyEvent.KEYCODE_ENTER && event.action == android.view.KeyEvent.ACTION_DOWN) {
                sendButton.performClick()
                return@setOnKeyListener true
            }
            false
        }
    }
}

package com.example.aiagent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // User-Nachricht hinzufügen
        addMessage(ChatMessage(text, isUser = true))

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.sendMessageToAI(text)
                .onSuccess { aiResponse ->
                    addMessage(ChatMessage(aiResponse, isUser = false))
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Fehler beim Senden der Nachricht"
                }

            _isLoading.value = false
        }
    }

    private fun addMessage(message: ChatMessage) {
        val currentList = _messages.value.orEmpty().toMutableList()
        currentList.add(message)
        _messages.value = currentList
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

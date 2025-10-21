package com.example.papb_bab5

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel dengan error handling yang lebih baik
 * Jawaban untuk pertanyaan eksplorasi #7
 */
class QuoteViewModelWithErrorHandling : ViewModel() {
    private val repository = QuoteRepository()

    private val _quote = MutableStateFlow("Tap the button to get a quote")
    val quote: StateFlow<String> get() = _quote

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchQuote() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _quote.value = "Loading..."

                val result = repository.getRandomQuote()
                _quote.value = result

            } catch (e: Exception) {
                // Tangani error agar aplikasi tidak crash
                _error.value = "Gagal memuat quote: ${e.message}"
                _quote.value = "Error occurred. Please try again."

                // Optional: Log error untuk debugging
                e.printStackTrace()

            } finally {
                // Pastikan loading state di-reset
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}


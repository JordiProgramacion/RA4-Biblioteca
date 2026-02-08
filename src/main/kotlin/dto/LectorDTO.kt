package dto

import models.Llibre

data class LectorDTO(
    val id: String,
    val nom: String,
    val llibresPrestatsLong: List<Long>,) {
}
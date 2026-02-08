package dto

data class LlibreDTO(
    val isbn: String,
    val titol: String,
    val autor: String,
    var estado: Boolean) {
}
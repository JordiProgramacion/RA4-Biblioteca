package models

class Lector(val nom: String) {
    // He tenido que hacer esta lista pública porque si estaba en privada no podía acceder a esta desde main sin usar setters o getters que todavía no entiendo.
    val llibresPrestats = mutableListOf<Llibre>()

    fun prestarLlibre(llibre: Llibre) {
        if (llibre.prestat) {
            println("El llibre ja esta prestat.")
        } else {
            llibresPrestats.add(llibre)
            llibre.prestar()
            println("Has prestat el llibre correctament.")
        }
    }
    fun llistarPrestecs() {
        if (llibresPrestats.isEmpty()) {
            println("No tens prestat ningun llibre.")
        } else {
            println("Els llibres prestats per $nom son: ")
            llibresPrestats.forEach { println(it) }
        }
    }
}
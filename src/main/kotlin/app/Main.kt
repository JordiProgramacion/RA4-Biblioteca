package app

import models.Llibre
import models.Lector
import core.Biblioteca
import utils.Utils

fun menu() {
    println("""
        -------------------------------
        Biblioteca d'Olesa
        -------------------------------
        1. Registrar llibre
        2. Registrar lector
        3. Prestar llibre
        4. Retornar llibre
        5. Llistar llibres disponibles
        6. Cercar llibres per autor 
        0. Sortir 
        -------------------------------
    """.trimIndent())
}
fun registrarLlibre(biblioteca: Biblioteca) {
    print("Títol del llibre: ")
    val titol = readln().replaceFirstChar { it.uppercase() }
    print("Autor: ")
    val autor = readln().replaceFirstChar { it.uppercase() }
    val llibre = Llibre(titol, autor)
    biblioteca.afegirLlibre(llibre)
}
fun registrarLector(biblioteca: Biblioteca) {
    print("Nom: ")
    val nom = readln().replaceFirstChar { it.uppercase() }
    val lector = Lector(nom)
    biblioteca.registrarLector(lector)
}
fun prestarLlibre(biblioteca: Biblioteca) {
    biblioteca.llistarDisponibles()
    print("Quin llibre et vols emportar: ")
    val titol = readln().replaceFirstChar { it.uppercase() }
    val llibre = biblioteca.cataleg.find { it.equals(titol) }
    if (llibre == null) {
        println("El llibre no es al catàleg.")
        return
    }
    if (llibre.prestat) {
        println("El llibre està prestat.")
        return
    }
    print("Nom del lector: ")
    val nom = readln().replaceFirstChar { it.uppercase() }
    val lector = biblioteca.lectors.find { it.equals(nom) }
    if (lector == null) {
        println("$nom no es un lector de la biblioteca.")
        return
    }
    lector.prestarLlibre(llibre)
    println("Llibre prestat correctament.")
}
fun retornarLlibre(biblioteca: Biblioteca) {
    println("Nom del lector: ")
    val nom = readln().replaceFirstChar { it.uppercase() }
    val nomLector = biblioteca.lectors.find { it.equals(nom) }
    if (nomLector == null) {
        println("$nom no es un lector de la biblioteca.")
        return
    }
    if (nomLector.llibresPrestats.isEmpty()) {
        println("$nomLector no té llibres prestats.")
        return
    }
    println("$nomLector té un total de ${Utils.comptarPrestats(nomLector.llibresPrestats)}")
    nomLector.llistarPrestecs()
    println("Títol del llibre: ")
    val titol = readln()
    val llibre = biblioteca.cataleg.find { it.titol == titol }
    if (llibre == null) {
        println("El llibre no existeix.")
        return
    }
    if (!llibre.prestat) {
        println("El llibre no esta prestat.")
        return
    }
    llibre.retornar()
    println(llibre.info())
    println("Llibre retornat correctament.")
}
fun cercarLlibresPerAutor(biblioteca: Biblioteca) {
    println("Nom de l'autor: ")
    val nom = readln().replaceFirstChar { it.uppercase() }
    val autor = biblioteca.cataleg.find { it.autor == (nom) }
    if (autor == null) {
        println("No tenim llibres d'aquest autor")
        return
    }
    val llistaLlibres = biblioteca.cercarPerAutor(nom)
    println("$nom te un total de: ${Utils.comptarPerAutor(biblioteca.cataleg, nom)} llibres en aquesta biblioteca.")
    println(llistaLlibres)
}
fun main() {
    val bibliotecaOlesa = Biblioteca()
    do {
        menu()
        print("\nQuina acció vols fer: ")
        val opcio = readln().toIntOrNull() ?: 10
        when (opcio) {
            1 -> registrarLlibre(bibliotecaOlesa)
            2 -> registrarLector(bibliotecaOlesa)
            3 -> prestarLlibre(bibliotecaOlesa)
            4 -> retornarLlibre(bibliotecaOlesa)
            5 -> bibliotecaOlesa.llistarDisponibles()
            6 -> cercarLlibresPerAutor(bibliotecaOlesa)
            0 -> println("Sortint de l'aplicació...")
            else -> println("Opció no valida.")
         }
    } while (opcio != 0)
}
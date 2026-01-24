package app

import models.Llibre
import models.Lector
import core.Biblioteca
import core.Persistencia
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
    print("Escriu qualsevol cosa per generar l'ISBN: ")
    val paraula = readln()
    val codi = generarCodi(paraula)
    biblioteca.cataleg.forEach {
        if (it.isbn == codi) {
            println("Introdueix un altra paraula, aquest ISBN ja existeix (quant més gran sigui el text més probable de que no es repeteixi.)")
            return
        }
    }
    val llibre = Llibre(titol = titol, autor = autor, isbn = codi)
    biblioteca.afegirLlibre(llibre)
    println("Llibre afegit correctament.")
}
fun registrarLector(biblioteca: Biblioteca) {
    print("Nom: ")
    val nom = readln().replaceFirstChar { it.uppercase() }
    print("Introdueix un id (mètode per identificarse al sistema): ")
    val id = readln()
    biblioteca.lectors.forEach {
        if (it.id == id) {
            print("Aquest id ja existeix, torna a intentar-ho.")
            return
        }
    }
    val lector = Lector(nom = nom, id = id)
    biblioteca.registrarLector(lector)
}
fun prestarLlibre(biblioteca: Biblioteca) {
    biblioteca.llistarDisponibles()
    print("ISBN: ")
    val isbn = readln().toLongOrNull()
    if (isbn == null) {
        println("Això no es un isbn.")
        return
    }
    val isbnReal = biblioteca.cataleg.find { it.isbn == (isbn) }
    if (isbnReal == null) {
        println("No existeix ningun llibre amb aquest isbn.")
        return
    }
    if (isbnReal.prestat) {
        println("Aquest llibre ja esta prestat en aquests moments.")
        return
    }
    print("Id del lector: ")
    val id = readln()
    val idReal = biblioteca.lectors.find { it.id == id }
    if (idReal == null) {
        println("Aquest id no es de ningun dels nostres lectors, si t'has oblidat del teu id vina a la secretaria de la biblioteca.")
        return
    }
    idReal.prestarLlibre(isbnReal)
    println("Llibre prestat correctament.")
}
fun retornarLlibre(biblioteca: Biblioteca) {
    print("Id del lector: ")
    val id = readln()
    val idLector = biblioteca.lectors.find { it.id == id }
    if (idLector == null) {
        println("$idLector no es un id de lector de la biblioteca.")
        return
    }
    if (idLector.llibresPrestats.isEmpty()) {
        println("$idLector no té llibres prestats.")
        return
    }
    println("${idLector.nom} té un total de ${Utils.comptarPrestats(idLector.llibresPrestats)} llibres prestats.")
    idLector.llistarPrestecs()
    print("ISBN del llibre: ")
    val titol = readln().toLongOrNull()
    if (titol == null) {
        println("No has introduït un isbn.")
        return
    }
    val isbnLlibre = biblioteca.cataleg.find { it.isbn == titol }
    if (isbnLlibre == null) {
        println("El ISBN es d'un llibre que no existeix.")
        return
    }
    if (!isbnLlibre.prestat) {
        println("El llibre no esta prestat.")
        return
    }
    idLector.retornarLlibre(isbnLlibre)
    println(isbnLlibre.info())
    println("Llibre retornat correctament.")
}
fun cercarLlibresPerAutor(biblioteca: Biblioteca) {
    print("Nom de l'autor: ")
    val nom = readln().replaceFirstChar { it.uppercase() }
    val autor = biblioteca.cataleg.find { it.autor == (nom) }
    if (autor == null) {
        println("No tenim llibres d'aquest autor")
        return
    }
    val llistaLlibres = biblioteca.cercarPerAutor(nom)
    println("$nom te un total de: ${Utils.comptarPerAutor(biblioteca.cataleg, nom)} llibre/s en aquesta biblioteca.")
    llistaLlibres.forEach { println(it.titol) }
}
fun generarCodi(palabra: String): Long {
    //val codigo = palabra.map {it.code}.joinToString("").toLong() Esto genera números muy grandes.
    val codigo = palabra.map {it.code}.sum().toLong()
    return codigo
}
fun main() {
    val bibliotecaOlesa = Persistencia.carregar()
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
            0 -> {
                Persistencia.guardar(bibliotecaOlesa)
                println("Sortint de l'aplicació...")
                Thread.sleep(2000)
            }
            else -> println("Opció no valida.")
         }
    } while (opcio != 0)
}
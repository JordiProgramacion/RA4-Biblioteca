package core

import models.Lector
import models.Llibre

class Biblioteca {

    val cataleg: MutableList<Llibre> = mutableListOf()
    val lectors: MutableList<Lector> = mutableListOf()

    fun afegirLlibre(llibre: Llibre): Boolean {
        if (llibre in cataleg) {
            println("El llibre ja es en el catàleg.")
            return false
        } else {
            cataleg.add(llibre)
            println("S'ha afegit al catàleg: ${llibre.info()}")
            return true
        }
    }
    fun registrarLector(lector: Lector): Boolean {
        if (lector in lectors) {
            println("Aquest lector ja ha sigut registrat.")
            return false
        } else {
            lectors.add(lector)
            println("El lector s'ha agregat correctament.")
            return true
        }
    }
    fun llistarDisponibles() {
        println("Els llibres disponibles son:")
        cataleg.forEach { if (!it.prestat) { println("${it.titol}, amb l'ISBN: ${it.isbn}") } }
    }
    fun cercarPerAutor(autor: String): List<Llibre> {
        val llibresAutors = mutableListOf<Llibre>()
        cataleg.forEach { if (it.autor == autor) {llibresAutors.add(it)} }
        return llibresAutors
    }
}
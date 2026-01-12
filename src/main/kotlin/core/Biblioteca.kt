package core

import models.Lector
import models.Llibre

class Biblioteca {

    val cataleg: MutableList<Llibre> = mutableListOf()
    val lectors: MutableList<Lector> = mutableListOf()

    fun afegirLlibre(llibre: Llibre) {
        if (llibre in cataleg) {
            println("El llibre ja es en el catàleg.")
        } else {
            cataleg.add(llibre)
            println("S'ha afegit al catàleg: ${llibre.info()}")
        }
    }
    fun registrarLector(lector: Lector) {
        if (lector in lectors) {
            println("Aquest lector ja ha sigut registrat.")
        } else {
            lectors.add(lector)
            println("El lector s'ha agregat correctament.")
        }
    }
    fun llistarDisponibles() {
        println("Els llibres disponibles son:")
        cataleg.forEach { if (!it.prestat) { println(it.titol) } }
    }
    fun cercarPerAutor(autor: String): List<Llibre> {
        val llibresAutors = mutableListOf<Llibre>()
        cataleg.forEach { if (it.autor == autor) {llibresAutors.add(it)} }
        return llibresAutors
    }
}
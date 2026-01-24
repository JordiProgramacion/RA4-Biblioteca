package core

import models.Lector
import models.Llibre
import java.io.File

object Persistencia {

    private const val FILE_NAME = "guardar.txt"

    fun guardar(biblioteca: Biblioteca) {
        val file = File(FILE_NAME)
        val sb = StringBuilder()
        biblioteca.cataleg.forEach {
            sb.appendLine("${it.isbn};${it.titol};${it.autor};${it.prestat}")
        }
        file.writeText(sb.toString())
        biblioteca.lectors.forEach {
            sb.appendLine("${it.id};${it.nom}")
        }
        file.writeText(sb.toString())
    }
    fun carregar(): Biblioteca {
        val llista = Biblioteca()
        val file = File(FILE_NAME)

        if (!file.exists()) {
            return llista
        }
        file.forEachLine {
            val parts = it.split(";")
            if (parts.size == 4) {
                val llibre = Llibre (
                    isbn = parts[0].toLong(),
                    titol = parts[1],
                    autor = parts[2],
                    prestat = parts[3].toBoolean()
                )
                llista.cataleg.add(llibre)
            }
            if (parts.size == 2) {
                val lector = Lector (
                    id = parts[0],
                    nom = parts[1]
                )
                llista.lectors.add(lector)
            }
        }
        return llista
    }
}
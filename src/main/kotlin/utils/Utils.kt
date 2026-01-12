package utils

import models.Llibre

class Utils {

    companion object {
        fun comptarPrestats(llista: List<Llibre>): Int {
            var totalLlibres = 0
            llista.forEach { if (it.prestat) { totalLlibres += 1 } }
            return totalLlibres
        }
        fun comptarPerAutor(llista: List<Llibre>, autor: String): Int {
            var totalLlibres = 0
            llista.forEach { if (it.autor == autor){ totalLlibres += 1 } }
            return totalLlibres
        }
    }
}
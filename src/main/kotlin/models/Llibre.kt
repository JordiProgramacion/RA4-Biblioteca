package models

class Llibre(val titol: String, val autor: String, var prestat: Boolean = false) {
    fun info():String {
        return "El llibre és $titol, autor: $autor i el llibre actualment ${if (prestat) "no està disponible" else "està disponible" }"
    }
    fun prestar() {
        prestat = true
    }
    fun retornar() {
        prestat = false
    }
}
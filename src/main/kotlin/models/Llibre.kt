package models

class Llibre(val isbn: Long, val titol: String, val autor: String, var prestat: Boolean = false) {
    fun info():String {
        return "$titol, autor: $autor, el llibre actualment ${if (prestat) "no està disponible" else "està disponible" } i el ISBN es $isbn"
    }
    fun prestar() {
        prestat = true
    }
    fun retornar() {
        prestat = false
    }
}
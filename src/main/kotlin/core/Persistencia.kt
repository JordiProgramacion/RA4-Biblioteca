package core

import models.Lector
import models.Llibre
import core.Biblioteca

    // Persistencia
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dto.LectorDTO
import java.io.File
import java.io.FileReader
import java.io.FileWriter

// import java.io.File // Llibreria per persistencia antigua.

object Persistencia {

    private val convertiroJson: Gson = GsonBuilder().setPrettyPrinting().create()
    private const val PERSISTENCIA_BIBLIOTECA = "persistencia.json"

    fun desarJSON(biblioteca: Biblioteca) {

        try {
            val file = File(PERSISTENCIA_BIBLIOTECA)

            FileWriter(file).use { escribir ->
                val lectorsData = biblioteca.lectors.map { lector ->
                    LectorDTO(
                        id = lector.id,
                        nom = lector.nom,
                        llibresPrestatsLong = lector.llibresPrestats.map { it.isbn }
                    )
                }
                val dades = mapOf(
                    "cataleg" to biblioteca.cataleg,
                    "lectors" to lectorsData
                )
                convertiroJson.toJson(dades, escribir)
            }
            println("S'han guardat totes les dades correctament.")
        } catch (e: Exception) {
            println("No s'han guardat correctament les dades: ${e.message}")
            // Esto es importante para saber que ha pasado, por qué y en que archivo
            e.printStackTrace()
        }

    }

    fun carregar(): Biblioteca {
        val llista = Biblioteca()
        val file = File(PERSISTENCIA_BIBLIOTECA)

        // Control de errores por si no existe el archivo
        if (!file.exists()) {
            return llista
        }

        try {
            FileReader(file).use { reader ->
                val tipus = object : TypeToken<Map<String, Any>>() {}.type
                val dades: Map<String, Any> = convertiroJson.fromJson(reader, tipus)

                // Cargamos el catálogo general donde no estan los prestamos
                val catalogType = object : TypeToken<List<Map<String, Any>>>() {}.type
                val catalogJson = convertiroJson.toJson(dades["cataleg"])
                val catalogList: List<Map<String, Any>> = convertiroJson.fromJson(catalogJson, catalogType)

                catalogList.forEach { llibreMap ->
                    val llibre = Llibre(
                        isbn = (llibreMap["isbn"] as Double).toLong(),
                        titol = llibreMap["titol"] as String,
                        autor = llibreMap["autor"] as String,
                        prestat = llibreMap["prestat"] as Boolean
                    )
                    llista.cataleg.add(llibre)
                }

                // Ahora tenemos que añadir a cada lector sus respectivos prestamos
                val lectorsJson = convertiroJson.toJson(dades["lectors"])
                val lectorsList: List<Map<String, Any>> = convertiroJson.fromJson(lectorsJson, catalogType)

                lectorsList.forEach { lectorMap ->
                    val lector = Lector(
                        id = lectorMap["id"] as String,
                        nom = lectorMap["nom"] as String
                    )

                    // Recuperar los ISBN de libros prestados
                    val llibresPrestatIds = lectorMap["llibresPrestatIds"] as? List<Double>

                    // Buscar cada libro en el catálogo y añadirlo a la lista del lector
                    llibresPrestatIds?.forEach { isbnDouble ->
                        val isbn = isbnDouble.toLong()
                        val llibre = llista.cataleg.find { it.isbn == isbn }
                        llibre?.let { lector.llibresPrestats.add(it) }
                    }

                    llista.lectors.add(lector)
                }
            }

        } catch (e: Exception) {
            println("Error al carregar les dades: ${e.message}")
            e.printStackTrace()
        }

        return llista
    }
// TODO
    /* DTO para archivos donde se guarden datos (los modelos)
    *  Los DTO son data class
    *  Import la libreria gson y guardarlo en un archivo json (la biblioteca)
    *  */
}




/* ---- Model de persistencia antic ----
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

 */
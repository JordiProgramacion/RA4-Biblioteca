package core

import models.Lector
import models.Llibre
import core.Biblioteca
import dto.BibliotecaDTO
import dto.LectorDTO
import dto.LlibreDTO
import dto.PrestecsDTO
    // Persistencia
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object Persistencia {

    val FILE_NAME = "persitencia.json"

    fun carregar(): Biblioteca {

        val file = File(FILE_NAME)
        val gson = Gson()
        val dto = gson.fromJson(file.readText(), BibliotecaDTO::class.java)
        val biblioteca = Biblioteca()

        dto.llistaLectors.forEach { lectorDTO ->
            biblioteca.registrarLector(Lector(lectorDTO.id, lectorDTO.nom))
        }
        dto.llistaLlibres.forEach { llibreDTO ->
            biblioteca.afegirLlibre(Llibre(llibreDTO.isbn, llibreDTO.titol, llibreDTO.autor))
        }
        dto.llistaPresetcs.forEach { prestecsDTO ->
            biblioteca.lectors.forEach { lector ->
                if (lector.id == prestecsDTO.id) {
                    biblioteca.cataleg.forEach { llibre ->
                        if (llibre.isbn == prestecsDTO.ISBN){
                            lector.prestarLlibre(llibre)
                        }
                    }
                }
            }
        }
        return biblioteca
    }
    fun desarJSON(biblioteca: Biblioteca) {

        val llistaLlibresDTO: MutableList<LlibreDTO> = mutableListOf()
        val llistaLectorsDTO: MutableList<LectorDTO> = mutableListOf()
        val llistaPrestecsDTO: MutableList<PrestecsDTO> = mutableListOf()
        val gson = GsonBuilder().setPrettyPrinting().create()

        biblioteca.cataleg.forEach { llibreDTO ->
            llistaLlibresDTO.add(LlibreDTO(llibreDTO.isbn, llibreDTO.titol, llibreDTO.autor))
        }
        biblioteca.lectors.forEach { lectorDTO ->
            llistaLectorsDTO.add(LectorDTO(lectorDTO.id, lectorDTO.nom))
            lectorDTO.llibresPrestats.forEach { prestecDTO ->
                llistaPrestecsDTO.add(PrestecsDTO(lectorDTO.id, prestecDTO.isbn))
            }
        }
        val bibliotecaDTO = BibliotecaDTO(llistaLlibresDTO, llistaLectorsDTO, llistaPrestecsDTO)
        File(FILE_NAME).writeText(gson.toJson(bibliotecaDTO))
    }
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
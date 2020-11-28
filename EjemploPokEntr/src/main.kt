import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList


fun main (){
    println("Bienvenido al sistema")
    //-------------------------------------------------------
    //Carga archivo de prueba
    var contenido = ""
    try {
        val filename = "ejemplo.txt"

        var file = File(filename)

        var fileExists = file.exists()

        if(fileExists){

            try {
                val archivo = InputStreamReader(filename)
                val br = BufferedReader(archivo)
                var linea = br.readLine()
                val todo = StringBuilder()
                while (linea != null) {
                    todo.append(linea + "\n")
                    linea = br.readLine()
                }
                br.close()
                archivo.close()

                contenido = todo.toString()

            } catch (e: IOException) {
            }


        } else { }
    }catch (e: IOException){}
    //--------------------------------------------------------
    //Cargar los archivos en listas

    //Cargar Entrenadores

    var listaEntrenadores: ArrayList<Entrenador>? = null
    //Cargar Pokemon
    var listaPokemon: ArrayList<Entrenador>
    //Cargar Capturas
    var listaCapturas : ArrayList<Captura>

    //-----------------------------------
/*
    var entrenador = Entrenador(
        idEntrenador: String,
        nombre: String,
        Genero: Char,
        fechaNac: Date,
        activo: Boolean
    )*/
    // Date(year: Int, month: Int, day: Int)
    var entrenador = Entrenador(
        "1725420895",
        "Ramses Paredes ",
        'M',
        Date(2020,7,30),
        true
    )

    //ImprimirEntrenadores
    if(listaEntrenadores!=null){
        listaEntrenadores.add(entrenador)
        listaEntrenadores.forEach{
                valorIteracion -> // SIN DEFINIR EL TIPO DE DATO (DUCK TYPING)
            //valorIteracion:Int -> sin definir tipo de dato (DUCK TYPING)
            println("Valor: ${valorIteracion}")
        }
    }

    //---------------------------------------------------------
    var seleccion: Int =0
    do{
        println("Seleccione una opcion: ")
        seleccion = readLine()?.toInt() as Int
        println("Su seleccion es: ${seleccion} ")

        when(seleccion){
            12 ->{ // se cumple esta condicion
                // Bloque de codigo
                println("Sueldo Normal")
            }
            -12 -> println("Sueldo negativo")
            else -> println("Sueldo no reconocido")
        }
    }while (true)
    //Variables inmutables
    // VAL (Preferidas)
    val numeroCedula = 12325454


    // Tipos de variables

}




// Fecha,Booleano, String, Entero, Decimal.
class Entrenador(
    idEntrenador: String,
    nombre: String,
    Genero: Char,
    fechaNac: Date,
    activo: Boolean

){

}
class Pokemon(
    nombre: String,
    Genero: Char,
    numPokedex: Int,
    lifePoints: Double

){

}

//puedo hacer historial de pokemon capturados

class Captura(
    entrenador: Entrenador,
    pokemon: Pokemon,
    fechaCaptura: Date,
    tipoPokeball: String
    //intentos de captura

){

}

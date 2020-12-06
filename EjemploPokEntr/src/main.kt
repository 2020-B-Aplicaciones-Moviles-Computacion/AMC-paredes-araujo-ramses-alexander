import java.io.*
import java.util.*
import kotlin.collections.ArrayList


fun main (){

    //------------------------------------
    //Creacion de listas para cargar datos
    //Lista de entrenadores
    val listaEntrenadores: ArrayList<Entrenador> = cargarFicheroEntrenador()
    //Cargar Pokemon
    var listaPokemon: ArrayList<Pokemon> = cargarFicheroPokemon()
    //Cargar Capturas
    var listaCapturas : ArrayList<Captura> = cargarFicheroCapturas(listaEntrenadores,listaPokemon)

    var seleccion: Int =0
    var seleccionRead:Int =0


    //---------------------------------------------------------
    //Comienza el sistema
    //---------------------------------------------------------
    println("Bienvenido al sistema")
    do{
        println("Seleccione una opcion: ")
        println("1- (Create) Ingresar un nuevo entrenador, pokemon o capturar\n" +
                "2- (Read)   Mostrar entrenadores, pokemon o capturas\n" +
                "3- (Update) Actualizar un entrenador, pokemon o una captura\n" +
                "4- (Delete) Eliminar un entrenador, pokemon o una captura\n")
        try {
            seleccion = readLine()?.toInt() as Int
            println("Su seleccion es: ${seleccion} ")
            when(seleccion){
                1->{
                    println("Desea Ingresar: " +
                            "\n1- Entrenadores" +
                            "\n2- Pokemon" +
                            "\n3- Capturar un pokemon")
                    try {
                        seleccionRead = readLine()?.toInt() as Int
                        println("Su seleccion es: ${seleccion} ")
                        when(seleccionRead){
                            1->{
                                var entrAux : Entrenador? = ingresoEntrenador()
                                if(entrAux!=null){
                                    listaEntrenadores.add(entrAux)
                                }
                            }
                            2->{
                                var pokAux : Pokemon? = ingresoPokemon()
                                if (pokAux!=null){
                                    listaPokemon.add(pokAux)
                                }
                            }
                            3->{
                                var entrCap= ""
                                var auxEntren: Entrenador? = null
                                //Aqui buscar el entrenador e ingresar el id del que queremos
                                println("Ingrese el id del entrenador que desea registrar una captura")
                                try {
                                    entrCap = readLine()?.toString() as String
                                    auxEntren = buscarEntrenadorID(listaEntrenadores,entrCap)
                                    if(auxEntren!=null){
                                        listaCapturas.add(capturaPokemon(auxEntren,listaPokemon))
                                    }else{
                                        println("El entrenador no existe")
                                    }
                                }catch (err: NumberFormatException){

                                }

                            }
                            else->{}

                        }

                    } catch (eRead1:NumberFormatException){
                        println("=================================================\n" +
                                "ERROR: Por favor ingrese correctamente una opcion\n"+
                                "=================================================\n"
                        )
                    }

                }
                2->{
                    println("Desea mostrar: " +
                            "\n1- Entrenadores" +
                            "\n2- Pokemon" +
                            "\n3- Capturas por entrenador")
                    try {
                        seleccionRead = readLine()?.toInt() as Int
                        println("Su seleccion es: ${seleccion} ")
                        when(seleccionRead){
                            1->{
                                println("Lista Entrenadores")
                                imprimirEntrenadores(listaEntrenadores)
                            }
                            2->{
                                println("Lista Pokemon")
                                imprimirPokemon(listaPokemon)
                            }
                            3->{
                                println("CAPTURAS")
                                imprimirCapturas(listaCapturas)
                            }
                            else->{}

                        }

                    } catch (eRead1:NumberFormatException){
                        println("=================================================\n" +
                                "ERROR: Por favor ingrese correctamente una opcion\n"+
                                "=================================================\n"
                        )
                    }



                }
                0->{
                    guardarFicheroEntrenador(listaEntrenadores)
                    guardarFicheroPokemon(listaPokemon)
                    guardarFicheroCaptura(listaCapturas )
                    System.exit(0)
                }
                else -> println("Esto aun no esta programado")
            }

        }catch (e2: NumberFormatException) {
            println("=================================================\n" +
                    "ERROR: Por favor ingrese correctamente una opcion\n"+
                    "=================================================\n"
            )
        }



    }while (true)
}


fun imprimirEntrenadores(
    listaEntrenadores: ArrayList<Entrenador>
){
    listaEntrenadores.forEach { entrenador->
        println("Valor: ${entrenador}") }
}
fun imprimirPokemon(
    listaPokemon: ArrayList<Pokemon>
){
    listaPokemon.forEach { pokemon->
        println("Valor: ${pokemon}") }
}
fun imprimirCapturas(listaCapturas: ArrayList<Captura>
){
    listaCapturas.forEach {captura->
        println("Valor: ${captura}")
    }

}


fun buscarEntrenadorID(
    listaEntrenadores: ArrayList<Entrenador>,
    idEntrenadorBus: String
) : Entrenador? {
    var entrenadorRes: Entrenador? = null
    listaEntrenadores.forEach { entrenador->
        //println("Valor: ${entrenador}")
        if (entrenador.idEntrenador.equals(idEntrenadorBus)){
            entrenadorRes = entrenador
        }
    }
    return entrenadorRes
}
fun buscarPokemonNumP(
    listaPokemon: ArrayList<Pokemon>,
    numPokBus: Int
): Pokemon? {
    var pokemonRes : Pokemon? = null
    listaPokemon.forEach { pokemon->
        //println("Valor: ${pokemon}")
        if(pokemon.nPokedex==numPokBus){
            pokemonRes = pokemon
        }
    }
    return pokemonRes
}
/*
    val idEntrenador: String?,
    val nombre: String?,
    val genero: Char?,
    val fechaNac: Date?,
    val activo: Boolean?*/

fun ingresoEntrenador(): Entrenador?{
    var idEntrenador: String? = ""
    var nombre: String? = ""
    var genero: Char? = null
    var fechaNac: String? = ""

    var flag = true
    var auxFech = 0

    var dia =0
    var mes =0
    var año =0
    try {
        println("Ingrese su ID")
        idEntrenador = readLine()?.toString() as String
        println("Ingrese su nombre (Solo letras)")
        nombre = readLine()?.toString() as String
        println("Ingrese su genero ( M - F )")
        genero = readLine()?.toCharArray()?.get(0)
        println("Ingrese su fecha de nacimiento aaaa/mm/dd")
        fechaNac = readLine()
        println("Por defecto el entrenador registrado sera puesto como Activo")


        val tokFecha = StringTokenizer(fechaNac,"/")
        while (tokFecha.hasMoreTokens()) {
            when(auxFech){
                0 ->{
                    año = tokFecha.nextToken().toInt()
                }
                1->{
                    mes = tokFecha.nextToken().toInt()
                }
                2->{
                    dia = tokFecha.nextToken().toInt()
                }
            }
            auxFech++
        }


    }catch (eRead1:NumberFormatException){
        println("=================================================\n" +
                "ERROR: Por favor ingrese correctamente lo solicitado\n"+
                "=================================================\n")
        flag = false
    }
        if (flag){
            return Entrenador(idEntrenador,nombre,genero, Date(año,mes,dia),true)
        }
    return null
}

/*
class Pokemon(
    val nPokedex: Int,
    val nombre: String,
    val genero: Char,
    val lifePoints: Double,
    val fuerza: Double*/


fun ingresoPokemon(): Pokemon?{
    var nPokedex: Int = 0
    var nombre: String = ""
    var genero: Char = ' '
    var lifePoints: Double = 0.0
    var fuerza: Double = 0.0

    var flag = true

    try {
        println("Ingrese el numero de pokedex")
        nPokedex = readLine()?.toInt() as Int
        println("Ingrese el nombre (Solo letras)")
        nombre = readLine()?.toString() as String
        println("Ingrese el genero ( M - F )")
        genero = readLine().toString().get(0)
        println("Ingrese los puntos de vida (0.0)")
        lifePoints = readLine()?.toDouble()!!
        println("Ingrese los puntos de fuerza (0.0)")
        fuerza = readLine()?.toDouble()!!

    }catch (eRead1:NumberFormatException){
        println("=================================================\n" +
                "ERROR: Por favor ingrese correctamente lo solicitado\n"+
                "=================================================\n")
        flag = false
    }
    if (flag){
        return Pokemon(nPokedex,nombre,genero,lifePoints,fuerza)
    }
    return null
}
/*class Captura(
    val entrenador: Entrenador,
    val pokemon: Pokemon,
    val fechaCaptura: Date,
    val tipoPokeball: String*/

fun capturaPokemon(
    entrenador: Entrenador,
    listaPokemon: ArrayList<Pokemon>
): Captura{
    var pokeball:String = ""
    var seleccion = 1
    try {
        println("Ingrese el tipo de pokeball que desea utilizar de pokedex\n" +
                "1- Pokeball")

        seleccion = readLine()?.toInt() as Int
        when(seleccion){
            1->{
                pokeball="PokeBall"
            }
            else->{
                println("No se comprendio, se utilizara una PokeBall")
            }
        }
    } catch (e: NumberFormatException ){
        pokeball="PokeBall"
    }
    println(" nununununununasdfasdunun ${listaPokemon.size}")
    return Captura(entrenador,listaPokemon.get((Math.random()*(listaPokemon.size)).toInt()),Date(),pokeball)
}

fun cargarFicheroEntrenador():ArrayList<Entrenador> {
    var archivo: File? = null
    var fr: FileReader? = null
    var br: BufferedReader? = null
    var listaEntrenadores: ArrayList<Entrenador> = ArrayList<Entrenador>()


    try {
        // Apertura del fichero y creacion de BufferedReader para poder
        // hacer una lectura comoda (disponer del metodo readLine()).
        archivo = File("C:\\Users\\USUARIO\\Desktop\\Universidad\\repos\\repaaac\\AMC-paredes-araujo-ramses-alexander\\EjemploPokEntr\\entrenadores.txt")
        fr = FileReader(archivo)
        br = BufferedReader(fr)

        //--------------------------------------------------------------
        var linea: String?
        while (br.readLine().also { linea = it } != null) {
            //println(linea)
            var auxLin =0
            var auxFech =0

            var carDia:Int =0
            var carAño:Int =0
            var carMes:Int =0

            var cargaID = ""
            var cargaNombre = ""
            var cargaGenero = ' '
            var cargaFecha = Date(2000,1,1)
            var cargaActiv = true

            val tokens = StringTokenizer(linea,"-")

            while (tokens.hasMoreTokens()) {
                when(auxLin){
                    0->{
                        cargaID = tokens.nextToken()
                    }
                    1->{
                        cargaNombre = tokens.nextToken()
                    }
                    2->{
                        cargaGenero =  tokens.nextToken().toCharArray()[0]
                    }
                    3->{
                        //Aqui se carga la fecha
                        val tokFecha = StringTokenizer(tokens.nextToken(),"/")
                        while (tokFecha.hasMoreTokens()) {
                            when(auxFech){
                                0 ->{

                                    carAño = tokFecha.nextToken().toInt()
                                }
                                1->{
                                    carMes = tokFecha.nextToken().toInt()
                                }
                                2->{
                                    carDia = tokFecha.nextToken().toInt()
                                }
                            }
                            auxFech++
                        }
                        cargaFecha = Date(carAño,carMes,carDia)
                        //cargaFecha = Date()
                        //println("--------------------------- (${carAño}/ ${carMes}/ ${carDia}) ++++ (${cargaFecha.year} / ${cargaFecha.month} / ${cargaFecha.date})")

                    }
                    4->{
                        var a = tokens.nextToken()
                        if (a.equals("true")){
                            cargaActiv = true
                        }else if (a.equals("false")){
                            cargaActiv = false
                        }

                    }
                }
                auxLin++
            }

            listaEntrenadores.add(
                Entrenador(
                    cargaID,
                    cargaNombre,
                    cargaGenero,
                    cargaFecha,
                    cargaActiv
                )
            )


        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        // En el finally cerramos
        // una excepcion.
        try {
            fr?.close()
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
    }
    return listaEntrenadores
}

fun cargarFicheroPokemon(): ArrayList<Pokemon> {

    var archivoPok: File? = null
    var frPok: FileReader? = null
    var brPok: BufferedReader? = null
    var listaPokemon  = ArrayList<Pokemon>()


    try {
        // Apertura del fichero y creacion de BufferedReader para poder
        // hacer una lectura comoda (disponer del metodo readLine()).
        archivoPok = File("C:\\Users\\USUARIO\\Desktop\\Universidad\\repos\\repaaac\\AMC-paredes-araujo-ramses-alexander\\EjemploPokEntr\\pokemon.txt")
        frPok = FileReader(archivoPok)
        brPok = BufferedReader(frPok)

        //--------------------------------------------------------------
        var linea: String?
        while (brPok.readLine().also { linea = it } != null) {
            //println(linea)
            var auxLin =0

            var carNPok:Int =0
            var carNom:String = ""
            var carGen:Char =' '
            var carLifPoin:Double = 0.0
            var carFuerza:Double = 0.0


            val tokens = StringTokenizer(linea,"-")

            while (tokens.hasMoreTokens()) {
                when(auxLin){
                    0->{
                        carNPok = tokens.nextToken().toInt()
                    }
                    1->{
                        carNom = tokens.nextToken()
                    }
                    2->{
                        carGen =  tokens.nextToken().toCharArray()[0]
                    }
                    3->{
                        carLifPoin = tokens.nextToken().toDouble()
                    }
                    4->{
                        carFuerza = tokens.nextToken().toDouble()
                    }
                }
                auxLin++
            }


            listaPokemon.add(
                Pokemon(
                    carNPok,
                    carNom,
                    carGen,
                    carLifPoin,
                    carFuerza
                )
            )


        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        // En el finally cerramos
        // una excepcion.
        try {
            frPok?.close()
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
    }
    return listaPokemon
}

fun cargarFicheroCapturas(
    listaEntrenadores: ArrayList<Entrenador>,
    listaPokemon: ArrayList<Pokemon>
): ArrayList<Captura> {
    var archivoCap: File? = null
    var frCap: FileReader? = null
    var brCap: BufferedReader? = null
    var listaCapturas = ArrayList<Captura>()


    try {
        // Apertura del fichero y creacion de BufferedReader para poder
        // hacer una lectura comoda (disponer del metodo readLine()).
        archivoCap = File("C:\\Users\\USUARIO\\Desktop\\Universidad\\repos\\repaaac\\AMC-paredes-araujo-ramses-alexander\\EjemploPokEntr\\captura.txt")
        frCap = FileReader(archivoCap)
        brCap = BufferedReader(frCap)

        //--------------------------------------------------------------
        var linea: String?
        while (brCap.readLine().also { linea = it } != null) {
            //println(linea)
            var auxLin =0
            var auxFech =0

            var carDia:Int =0
            var carAño:Int =0
            var carMes:Int =0

            var cargaEntrenador: Entrenador? = null
            var cargaPokemon: Pokemon? = null
            var cargaFechaCap = Date(2000,1,1)
            var cargaTipoPokeball = ""

            val tokens = StringTokenizer(linea,"-")

            while (tokens.hasMoreTokens()) {
                when(auxLin){
                    0->{
                        cargaEntrenador = buscarEntrenadorID(listaEntrenadores,tokens.nextToken())
                    }
                    1->{
                        cargaPokemon = buscarPokemonNumP(listaPokemon,tokens.nextToken().toInt())
                    }
                    2->{
                        //Aqui se carga la fecha
                        val tokFecha = StringTokenizer(tokens.nextToken(),"/")
                        while (tokFecha.hasMoreTokens()) {
                            when(auxFech){
                                0 ->{
                                    carAño = tokFecha.nextToken().toInt()
                                }
                                1->{
                                    carMes = tokFecha.nextToken().toInt()
                                }
                                2->{
                                    carDia = tokFecha.nextToken().toInt()
                                }
                            }
                            auxFech++
                        }
                        cargaFechaCap = Date(carAño,carMes,carDia)
                        //cargaFecha = Date()
                        //println("--------------------------- (${carAño}/ ${carMes}/ ${carDia}) ++++ (${cargaFechaCap.year} / ${cargaFechaCap.month} / ${cargaFechaCap.date})")

                    }
                    3->{
                        cargaTipoPokeball = tokens.nextToken()

                    }
                }
                auxLin++
            }
            if(cargaEntrenador!= null){
                if(cargaPokemon!=null){
                    listaCapturas.add(
                        Captura(
                            cargaEntrenador,
                            cargaPokemon,
                            cargaFechaCap,
                            cargaTipoPokeball
                        )
                    )
                }
            }

        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        // En el finally cerramos
        // una excepcion.
        try {
            frCap?.close()
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
    }
    return listaCapturas
}


fun guardarFicheroEntrenador(
    listaEntrenadores: ArrayList<Entrenador>
){
    //Llenamos todos los datos del array en un string para eso guardarlo en el archivo
    //---------------------------------------------------------------------------------
    var stringEntrenadores = ""
    listaEntrenadores.forEach { entrenador->
        stringEntrenadores=stringEntrenadores+"" +
                "${entrenador.idEntrenador}-" +
                "${entrenador.nombre}-" +
                "${entrenador.genero}-" +
                "${entrenador.fechaNac?.year}/${entrenador.fechaNac?.month}/${entrenador.fechaNac?.date}-" +
                "${entrenador.activo}\n"
    }
    //---------------------------------------------------------------------------------
    var flwriter: FileWriter? = null
    try {
        //crea el flujo para escribir en el archivo
        flwriter = FileWriter("C:\\Users\\USUARIO\\Desktop\\Universidad\\repos\\repaaac\\AMC-paredes-araujo-ramses-alexander\\EjemploPokEntr\\entrenadores.txt")
        //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
        val bfwriter = BufferedWriter(flwriter)

        bfwriter.write(stringEntrenadores)
        //cierra el buffer intermedio
        bfwriter.close()
        println("Archivo creado satisfactoriamente..")
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        if (flwriter != null) {
            try { //cierra el flujo principal
                flwriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

fun guardarFicheroPokemon(
    listaPokemon: ArrayList<Pokemon>
){
    //Llenamos todos los datos del array en un string para eso guardarlo en el archivo
    //---------------------------------------------------------------------------------
    var stringPokemon = ""
    listaPokemon.forEach { pokemon->
        stringPokemon=stringPokemon+"" +
                "${pokemon.nPokedex}-" +
                "${pokemon.nombre}-" +
                "${pokemon.genero}-" +
                "${pokemon.lifePoints}-" +
                "${pokemon.fuerza}\n"
    }
    //---------------------------------------------------------------------------------
    var flwriter: FileWriter? = null
    try {
        //crea el flujo para escribir en el archivo
        flwriter = FileWriter("C:\\Users\\USUARIO\\Desktop\\Universidad\\repos\\repaaac\\AMC-paredes-araujo-ramses-alexander\\EjemploPokEntr\\pokemon.txt")
        //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
        val bfwriter = BufferedWriter(flwriter)

        bfwriter.write(stringPokemon)
        //cierra el buffer intermedio
        bfwriter.close()
        println("Archivo creado satisfactoriamente..")
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        if (flwriter != null) {
            try { //cierra el flujo principal
                flwriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

fun guardarFicheroCaptura(
    listaCapturas: ArrayList<Captura>
){
    //Llenamos todos los datos del array en un string para eso guardarlo en el archivo
    //---------------------------------------------------------------------------------
    // 1725420895-112-2020/4/12-ultraball
    //1725420768-112-2020/1/12-superball
    var stringEntrenadores = ""
    listaCapturas.forEach { captura->
        stringEntrenadores=stringEntrenadores+"" +
                "${captura.entrenador.idEntrenador}-" +
                "${captura.pokemon.nPokedex}-" +
                "${captura.fechaCaptura?.year}/${captura.fechaCaptura?.month}/${captura.fechaCaptura?.date}-" +
                "${captura.tipoPokeball}\n"
    }
    //---------------------------------------------------------------------------------
    var flwriter: FileWriter? = null
    try {
        //crea el flujo para escribir en el archivo
        flwriter = FileWriter("C:\\Users\\USUARIO\\Desktop\\Universidad\\repos\\repaaac\\AMC-paredes-araujo-ramses-alexander\\EjemploPokEntr\\captura.txt")
        //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
        val bfwriter = BufferedWriter(flwriter)

        bfwriter.write(stringEntrenadores)
        //cierra el buffer intermedio
        bfwriter.close()
        println("Archivo creado satisfactoriamente..")
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        if (flwriter != null) {
            try { //cierra el flujo principal
                flwriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}




// Fecha,Booleano, String, Entero, Decimal.
class Entrenador(
    val idEntrenador: String?,
    val nombre: String?,
    val genero: Char?,
    val fechaNac: Date?,
    val activo: Boolean?

){

    override fun toString(): String {
        val linea = "\n\tID: \t\t${idEntrenador}\n" +
                "\tNombre: \t${nombre}\n" +
                "\tGenero: \t${genero}\n" +
                "\tNacimiento:  ( ${fechaNac?.year} / ${fechaNac?.month} / ${fechaNac?.date} )\n" +
                "\tActivo: \t${activo}\n"
        return linea
    }
}
class Pokemon(
    val nPokedex: Int,
    val nombre: String,
    val genero: Char,
    val lifePoints: Double,
    val fuerza: Double
){
    override fun toString(): String {
        val linea = "\n" +
                "\tNPokedex:\t${nPokedex}\n" +
                "\tNombre:  \t${nombre}\n" +
                "\tGenero:  \t${genero}\n" +
                "\tLifePnts:\t${lifePoints}\n"+
                "\tFuerza:  \t${fuerza}\n"
        return linea
    }
}

//puedo hacer historial de pokemon capturados

class Captura(
    val entrenador: Entrenador,
    val pokemon: Pokemon,
    val fechaCaptura: Date,
    val tipoPokeball: String

){
    override fun toString(): String {
        val linea = "\n" +
                "\tEntrenador:    ${entrenador.nombre} ${entrenador.idEntrenador}\n" +
                "\tPokemon:       ${pokemon.nombre} ${pokemon.nPokedex}\n" +
                "\tFecha captura: ( ${fechaCaptura?.year} / ${fechaCaptura?.month} / ${fechaCaptura?.date} )\n" +
                "\tTipo pokeball: ${tipoPokeball}"
        return linea
    }
}

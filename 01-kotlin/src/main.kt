import java.util.*
import kotlin.collections.ArrayList

fun main (){
    println("Hola mundo")
    var edadProfesor =31;
    var sueldoProfesor:Double = 23.2
    //var sueldoProfesor:Double = 23.2

    //VARIABLES MUTABLES (reasignando los valores)
    // VAR
    var edadCachorro:Int
    edadCachorro = 1

    //Variables inmutables
    // VAL (Preferidas)
    val numeroCedula = 12325454


    // Tipos de variables

    val nombreProfesor: String = "Adrian"
    val sueldo: Double = 12.2
    val estadoCivil: Char = 'S'
    val fechaNacimiento = Date()

    // Condicionales
    if( sueldo ==12.2){

    }else{

    }
    when(sueldo){
        12.2 ->{ // se cumple esta condicion
                 // Bloque de codigo
            println("Sueldo Normal")
        }
        -12.2 -> println("Sueldo negativo")
        else -> println("Sueldo no reconocido")
    }
    //expresion dentro de una misma linea

    val sueldoMayorAlEstablecido:Boolean = if (sueldo==12.2)false else true
    //val sueldoMayorAlEstablecido:Boolean = if (sueldo==12.2) 2  else 20
    // condicion ? bloque-true : bloque- false


    //enviar funcion
    imprimirNombre("Adrian")
    calcularSueldo(1000.0,14.00,3)

    // para solo enviar sueldo y calculo especial
    //Parametros nombrados
    calcularSueldo(1000.0,
        //14.00
        calculoEspecial = 3)

    //ARREGLOS

    //Arreglo inmutable (estatico)
    val arregloInmutable : Array<Int> = arrayOf(1,2,3)

    //Arreglo mutable (dinamico)
    //cntr + alt + l
    val arregloMutable : ArrayList<Int> = arrayListOf(1,2,3,4,5,6,7,8,8,10)
    println(arregloMutable)
    arregloMutable.add(11)
    arregloMutable.add(12)
    println(arregloMutable)
    
    // XXXXXX OPERADORES
    //Ayudan a trabajar con arreglos estaticos y dinamicos

    //arregloMutable.forEach()
    //arregloInmutable.forEach()
    // VOID ES UNIT

    val respuestaForEach:Unit = arregloMutable.forEach{
        valorIteracion -> // SIN DEFINIR EL TIPO DE DATO (DUCK TYPING)
        //valorIteracion:Int -> sin definir tipo de dato (DUCK TYPING)
        println("Valor: ${valorIteracion}")
    }
    //OPERADOR FOREACH INDEXED
    val respuestaForEachIndexed:Unit  =arregloMutable.forEachIndexed{
            indice,valorIteracion -> // SIN DEFINIR EL TIPO DE DATO (DUCK TYPING)
        //valorIteracion:Int -> sin definir tipo de dato (DUCK TYPING)
        println("Valor: ${valorIteracion}  Indice:${indice}")
    }

    //OPERADOR MAP
    //1)Enviemos el nuevo valor de la iteracion
    //2)Nos devuelve un nuevo arreglo con los valores modificados

    val respuestaMap: List<Int> = arregloMutable.map {  valorActualIteracion ->
        return@map valorActualIteracion*10
    }
    println(respuestaMap)
    println("---------------------------")
    arregloMutable.map {  valorActualIteracion -> valorActualIteracion + 15
    }

    //OPERADOR FILTER
    //FILTER - FILTRA EL ARREGLO
    //1) Devuelve una expresion True o False
    //2) Nuevo arreglo filtrado

    val respuestaFilter: List<Int> =arregloMutable.filter {  valorActualIteracion ->
        val mayoresACinco : Boolean = valorActualIteracion > 5
        return@filter mayoresACinco
    }

    println(respuestaFilter)
    arregloMutable.filter { i->i>5 }
    arregloMutable.filter { it>5 }

    //--------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------
    //Otro operador
    //Any All -> Revisar una condicion dentro del arreglo (tablas de tautollogia)
    //OR - AND
    //OR = ANY
    //OR(Falso - TODOS TIENEN QUE SER FALSO)
    //OR (TRUE - UNO ES TRUE YA ES TRUE)
    //AND (FALSO - SI UNO ES FALSO)
    //AND (TRUE - TODOS SON TRUE ES TRUE)
    //AND = All

    val respuestaAny: Boolean = arregloMutable
        .any {valorActualIteracion ->
            return@any valorActualIteracion>12
        }
    println(arregloMutable)
    println(respuestaAny)


    val respuestaAll: Boolean = arregloMutable
        .all {valorActualIteracion ->
            return@all valorActualIteracion<=12
        }
    println(arregloMutable)
    println(respuestaAll)

    //REDUCE
    //1) Devuelve el acumulado
    //2)En que valor empieza
    // acumula los valores del array
    // [1,2,3,4,5]

    val respuestaReduce = arregloMutable
        .reduce{ acumulado,valorActualIteracion->
            return@reduce acumulado + valorActualIteracion
        }
    println(arregloMutable)
    println(respuestaReduce)


    val respuestaReduceFold = arregloMutable
        .fold(
            100,
            {acumulado, valorActualIteracion ->
                return@fold acumulado - valorActualIteracion
            }
        )
    println(arregloMutable)
    println(respuestaReduceFold)
    //tenemos el fold normal
    // y el fold rigth
    // empieza desde el final
    // foldRigth
    // reduce
    // reduceRigth



    //OPERADORES
    // forEac
    //paradigma de programacion funcional
    val vidaActual = arregloMutable
        .map { it*1.2 }
        .filter { it >20 }
        .fold(100,{acc,i -> (acc - i).toInt() })
        .also { println(it) }

    //------------------------- uso de las clases

    val ejemploUno = Suma(1,2,3)
    val ejemploDos = Suma(1,null,3)
    val ejemploTres = Suma(null,null,null)
    println(ejemploUno.sumar())
    println(Suma.historialDeSumas)
    println(ejemploDos.sumar())
    println(Suma.historialDeSumas)
    println(ejemploTres.sumar())
    println(Suma.historialDeSumas)






} //main cerrado

//FUNCIONES

fun imprimirNombre(nombre:String){
    println("Nombre: ${nombre}")
}

fun calcularSueldo(
    sueldo:Double, //requerido
    tasa:Double = 12.00, //opcionales
    calculoEspecial: Int? = null //calculo especial es un entero
                                    // con valor inicial de "null"

): Double{ // lo que se va a devolver

    // Int -> Int? (Puede ser nulo)
    // String -> String? (Puede ser nulo)
    // Date -> Date? (Puede ser nulo)

    if (calculoEspecial == null){
        return sueldo*(100/tasa)
    }else{
        return sueldo*(100/tasa)* calculoEspecial
    }


    //CLASE ABSTRACTA
    //estilo kotlin
    abstract class NumerosJava{
        protected val numeroUno: Int
        private val numeroDos: Int
        constructor( //constructor primario
            uno:Int, dos:Int
        ){ //bloque de codigo del constructor primario

            numeroUno = uno
            numeroDos = dos
            println(numeroUno)
            println(numeroDos)
        }
    }


}

//CLSES abstractas
    abstract class Numeros(
        protected var numeroUno:Int,
        protected var numeroDos:Int
    ){
        init {
            println(numeroUno)
            println(numeroDos)
        }
    }



    class  Suma(
        uno: Int,
        dos:Int,
        protected var tres: Int
    ): Numeros(uno,dos){ //Numeros es una clase super, que almenos necesita ser inicializada con 2 valores
        init {
            println("Constructor primario")
        }
        //segundo constructor
        //en este el dos puede ser nulo
        constructor(
            uno:Int,
            dos:Int?,
            tres:Int
        ):this( //llamada al constructor primario
            uno,
            if (dos == null) 0 else dos,
            tres){
        }

        //Trcer constructor
        //en este los tres pueden ser nulos
        constructor(
            uno:Int?,
            dos:Int?,
            tres:Int?
        ):this( //llamada al constructor primario
            if (uno == null) 0 else uno,
            if (dos == null) 0 else dos,
            if (tres == null) 0 else tres){
        }


        public fun sumar(): Int{
            this.tres
            val total = this.numeroUno + this.numeroDos
            Suma.agregarHistorial(total)
            return total
        }
        companion object{ //SINGLETON va a existir haya o no instancias
                            // sera el mismo para toda la clase
            val historialDeSumas = arrayListOf<Int>()
            fun agregarHistorial(nuevaSuma:Int){
                this.historialDeSumas.add(nuevaSuma)
            }
        }
    }

    //Clase generalmente utilizada para cargar datos
    // para cargar datos
    class BaseDeDatos(){
        companion object{
            val datos = arrayListOf<Int>()
        }
    }




package com.example.aplicacionmoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result

class HHttpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_h_http)

        //metodoGet()

        //metodoPost()

        //metodoDelete(7)
        val parametrosAct : List<Pair<String,*>> = listOf(
                "title" to "tituloActualizado",
                "body" to "descripcionActualizada",
                "userId" to 1
        )
        //metodoPut(1,parametrosAct)

        metodoGetBusqueda(4)

        //Eliminar Actualizar Buscar por userID

    }
        fun metodoGet(){
  /*          "https://jsonplaceholder.typicode.com/posts/1"
                .httpGet()
                .responseString{req,res,result ->
                    when(result){
                        is Result.Failure ->{
                            val error = result.get()
                            Log.i("http-klaxon","Error")
                        }
                        is Result.Success ->{
                            val postString = result.get()
                            Log.i("http-klaxon","${postString}")
                            //------klaxon
                            val post = Klaxon()
                                .parse<IPostHttp>(postString)
                            Log.i("http-klaxon","Titulo__ ${post?.title}")

                        }
                    }
        }
*/
            "https://jsonplaceholder.typicode.com/posts"
                .httpGet()
                .responseString{req,res,result ->
                    when(result){
                        is Result.Failure ->{
                            Log.i("http-klaxon-GET","Error")
                        }
                        is Result.Success ->{
                            val postString = result.get()
                           // Log.i("http-klaxon","${postString}")
                            //------klaxon
                            val arrPost = Klaxon()
                                .parseArray<IPostHttp>(postString)
                            if(arrPost != null){
                                arrPost.forEach{
                                    Log.i("http-klaxon-GET","GET: IDUsuario: ${it.userId}" +
                                            "\nID: ${it.id}"+
                                            "\nTitulo:  ${it.title} " +
                                            "\nBody: ${it.body} ")
                                }
                            }
                        }
                    }
                }
    }


    fun metodoPost(){
        val parametros : List<Pair<String,*>> = listOf(
            "title" to "tit",
            "body" to "descripcion",
            "userId" to 1
        )
        "https://jsonplaceholder.typicode.com/posts"
            .httpPost(parametros)
            .responseString{
                req,res,result->
                when(result){
                    is Result.Failure ->{
                        val error = result.get()
                        Log.i("http-klaxon-Post","Error")
                    }
                    is Result.Success ->{
                        val postString = result.get()
                        Log.i("http-klaxon-POST","${postString}")
                        val arrPost = Klaxon()
                            .parse<IPostHttp>(postString)
                        Log.i("http-klaxon-POST","POST: IDUsuario: ${arrPost?.userId}" +
                                "\nID:${arrPost?.id}" +
                                "\nTitle: ${arrPost?.title}" +
                                "\nBody: ${arrPost?.body}")

                    }
                }
            }
    }

    fun metodoDelete(idEliminar: Int ){

        "https://jsonplaceholder.typicode.com/posts/${idEliminar}"
                .httpDelete()
                .responseString{req,res,result ->
                    when(result){
                        is Result.Failure ->{
                            Log.i("http-klaxon-DELETE","Error")
                        }
                        is Result.Success ->{
                            Log.i("http-klaxon-DELETE","Se elimino correctamente")
                            val postString = result.get()
                            Log.i("http-klaxon-DELETE","Se tiene: ${postString}")
                        }
                    }
                }
    }


    fun metodoPut(idActualizar:Int,parametros:List<Pair<String,*>>){

        "https://jsonplaceholder.typicode.com/posts/${idActualizar}"
                .httpPut(parametros)
                .responseString{
                    req,res,result->
                    when(result){
                        is Result.Failure ->{
                            val error = result.get()
                            Log.i("http-klaxon-PUT","Error")
                        }
                        is Result.Success ->{
                            val postString = result.get()
                            Log.i("http-klaxon-PUT","${postString}")
                            val arrPost = Klaxon()
                                    .parse<IPostHttp>(postString)
                            Log.i("http-klaxon-PUT","POST: IDUsuario: ${arrPost?.userId}" +
                                    "\nID:${arrPost?.id}" +
                                    "\nTitle: ${arrPost?.title}" +
                                    "\nBody: ${arrPost?.body}")

                        }
                    }
                }
    }


    fun metodoGetBusqueda(idUsuaioBusq:Int){

        "https://jsonplaceholder.typicode.com/posts?userId=${idUsuaioBusq}"
                .httpGet()
                .responseString{req,res,result ->
                    when(result){
                        is Result.Failure ->{
                            Log.i("http-klaxon-GetBusqueda","Error")
                        }
                        is Result.Success ->{
                            val postString = result.get()
                            // Log.i("http-klaxon","${postString}")
                            //------klaxon
                            val arrPost = Klaxon()
                                    .parseArray<IPostHttp>(postString)
                            if(arrPost != null){
                                arrPost.forEach{
                                    Log.i("http-klaxon-GetBusqueda","GET Busqueda: IDUsuario: ${it.userId} " +
                                            "\nID: ${it.id}"+
                                            "\nTitulo:  ${it.title} " +
                                            "\nBody: ${it.body} ")
                                }
                            }
                        }
                    }
                }

    }

}
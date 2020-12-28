package com.example.aplicacionmoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

class HHttpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_h_http)

        metodoGet()

        //metodoPost()


    }
        fun metodoGet(){
   /*         "https://jsonplaceholder.typicode.com/posts/1"
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
            "https://jsonplaceholder.typicode.com/posts/1"
                .httpGet()
                .responseString{req,res,result ->
                    when(result){
                        is Result.Failure ->{
                            Log.i("http-klaxon","Error")
                        }
                        is Result.Success ->{
                            val postString = result.get()
                           // Log.i("http-klaxon","${postString}")
                            //------klaxon
                            val arrPost = Klaxon()
                                .parseArray<IPostHttp>(postString)
                            if(arrPost != null){
                                arrPost.forEach{
                                    Log.i("http-klaxon-1","Titulo__ ${it.title}")
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
                        Log.i("http-klaxon-Post","${postString}")
                        val arrPost = Klaxon()
                            .parse<IPostHttp>(postString)
                        Log.i("http-klaxon-1","Titulo__ ${arrPost?.title}")

                    }
                }
            }
    }
}
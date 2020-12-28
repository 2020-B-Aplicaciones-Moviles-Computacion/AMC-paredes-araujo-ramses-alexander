package com.example.aplicacionmoviles

class IPostHttp (
    val id:Int,
    var userId:Any,
    val title:String,
    val body:String
){
var userIdTransformado: Int = 0;
    init {

       if (userId is String){
           userId = (userId as String).toInt()
       }
        if (userId is Int){
            userId=userId
        }
    }
}

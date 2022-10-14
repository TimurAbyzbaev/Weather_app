package ru.abyzbaev.weather_app.LESSON3

import android.util.Log

class Lesson3 { //Дженерик это обобщение
    fun GenericLesson(){
        var phraseList = listOf<String>()

        some(1)
        some("a")
        some(listOf("@2", "23"))

    }

    fun<T> some(input:T){
        Log.d("@@@", input.toString())
    }
    val producer = Producer<Double>()


}

/*
in только потребление
out только выдача
 */

class Producer<out T>{
    val list:List<T> = listOf<T>()
    fun produce():T{
        return list.first()
    }

}
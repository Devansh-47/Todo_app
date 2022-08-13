package com.example.todouu
//import android.content.Context
//
//
//
//abstract class Appdatabase: RoomDatabase() {
//    abstract fun Tododao():tododao
//
//    companion object{
//        @Volatile
//        private var instance: Appdatabase?=null
//
//        fun getdatabase(context:Context):Appdatabase{
//            val tempinstance= instance
//            if(tempinstance!=null){
//                return tempinstance
//            }
//
//            synchronized(this){
//                 val instancee= Room.databaseBuilder(context.applicationContext,Appdatabase::class.java,"todo.db").build()
//                instance=instancee
//                return instance as Appdatabase
//            }
//        }
//    }
//
//}
//

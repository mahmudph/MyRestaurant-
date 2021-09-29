package com.example.restaurantreview.event

open class Event<out T>(private val content: T) {
    var hasBeenHandle = false
    private set

    fun getContentIfNotHandled(): T? {
        return if(hasBeenHandle) {
            null
        } else {
            hasBeenHandle = true
            content
        }
    }

    fun peekContent(): T = content
}
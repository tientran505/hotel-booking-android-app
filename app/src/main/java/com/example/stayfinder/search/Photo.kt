package com.example.stayfinder.search

class Photo(private var resourceId: Int) {
    fun getResourceId(): Int {
        return resourceId
    }

    fun setResourceId(resourceId: Int) {
        this.resourceId = resourceId
    }
}
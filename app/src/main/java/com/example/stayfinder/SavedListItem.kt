package com.example.stayfinder

class SavedListItem:java.io.Serializable {
    public var titlename: String? = ""
    public var name: String? = ""
    public var img: Int? = null

    constructor(stitlename: String, sname: String, simg: Int){
        titlename = stitlename
        name = sname
        img = simg
    }
}
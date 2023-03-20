package com.example.stayfinder
class SavedList:java.io.Serializable{
    public var titlename: String? = ""
    public var props: String? = ""

    constructor(stitlename: String, sname: String){
        titlename = stitlename
        props = sname
    }
}






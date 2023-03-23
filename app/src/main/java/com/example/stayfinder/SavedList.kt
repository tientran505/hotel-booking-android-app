package com.example.stayfinder
class SavedList:java.io.Serializable{
    public var titlename: String? = ""
    public var props: String? = ""
    var items : List<SavedListItem>? = null

    constructor(stitlename: String, sname: String){
        titlename = stitlename
        props = sname
    }
}






package com.example.stayfinder

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

class SavedList:java.io.Serializable{
    public var titlename: String? = ""
    public var props: String? = ""
    var id: String?=""
    var items : List<SavedListItem>? = null

    constructor(stitlename: String, sname: String, sid:String){
        titlename = stitlename
        props = sname
        id = sid
    }

    override fun toString(): String {
        return titlename + " " + props
    }
}

@IgnoreExtraProperties
data class saved_lists(
    var id: String = "",
    var user_id:String = "",
    var name_list:String = "",
    var number_of_item:Int = 0,
    var create_date: Timestamp = Timestamp.now()
): Serializable {
    //constructor(id:String, user_id: String, name_list: String) : this(id, user_id, name_list, 0)
    //constructor():this("","","",0)
    //constructor(a:saved_lists):this(a.id,a.user_id,a.name_list,a.number_of_item)
}








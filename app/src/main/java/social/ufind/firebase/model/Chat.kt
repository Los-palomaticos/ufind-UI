package social.ufind.firebase.model

data class Chat(val messages: ArrayList<Message>){

        constructor(): this(arrayListOf<Message>())


}

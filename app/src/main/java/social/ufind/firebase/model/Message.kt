package social.ufind.firebase.model

data class Message(val message: String, val email: String) {
    constructor() : this("", "")
}

package social.ufind.firebase.model

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import java.io.Serializable

data class User(val email: String, val name: String) {
    constructor() : this("", "")
}

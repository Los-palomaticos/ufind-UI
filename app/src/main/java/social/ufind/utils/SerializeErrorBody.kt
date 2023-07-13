package social.ufind.utils

import com.google.gson.Gson
import retrofit2.HttpException

class SerializeErrorBody() {
    companion object {
        fun <T> getSerializedError(e: HttpException, obj: Class<T>): T {
            val errors = e.response()?.errorBody()?.string().toString()
            return Gson().fromJson(errors, obj)
        }
    }
}
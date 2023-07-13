package social.ufind.network.dto.signup

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
)

package cl.duoc.wim100.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Modelos de datos para mapear la respuesta JSON de la API Externa (RandomUser API)
data class RandomUserResponse(
    @SerializedName("results") val results: List<RandomUserResult>
)

data class RandomUserResult(
    @SerializedName("name") val name: UserName,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("location") val location: UserLocation,
    @SerializedName("picture") val picture: UserPicture
)

data class UserName(
    @SerializedName("first") val first: String,
    @SerializedName("last") val last: String
)

data class UserLocation(
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String
)

data class UserPicture(
    @SerializedName("large") val large: String,
    @SerializedName("medium") val medium: String
)

// Interfaz Retrofit para la API Externa
interface ExternalApiService {
    @GET("api/")
    suspend fun getRandomCandidates(
        @Query("results") count: Int = 5,
        @Query("inc") fields: String = "name,email,phone,location,picture"
    ): Response<RandomUserResponse>
}

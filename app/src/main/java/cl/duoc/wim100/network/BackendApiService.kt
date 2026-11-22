package cl.duoc.wim100.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

// DTO para enviar y recibir datos del Microservicio Spring Boot
data class CandidateDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("rut") val rut: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("experienceYears") val experienceYears: Int,
    @SerializedName("profession") val profession: String,
    @SerializedName("status") val status: String = "Pendiente",
    @SerializedName("score") val score: Int = 0,
    @SerializedName("feedback") val feedback: String = ""
)

// Interfaz Retrofit para el Microservicio propio (Spring Boot)
interface BackendApiService {
    @GET("api/candidates")
    suspend fun getAllCandidates(): Response<List<CandidateDto>>

    @GET("api/candidates/{id}")
    suspend fun getCandidateById(@Path("id") id: Int): Response<CandidateDto>

    @POST("api/candidates")
    suspend fun createCandidate(@Body candidate: CandidateDto): Response<CandidateDto>

    @PUT("api/candidates/{id}")
    suspend fun updateCandidate(@Path("id") id: Int, @Body candidate: CandidateDto): Response<CandidateDto>

    @DELETE("api/candidates/{id}")
    suspend fun deleteCandidate(@Path("id") id: Int): Response<Void>
}

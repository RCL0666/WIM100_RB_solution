package cl.duoc.wim100.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidates")
data class Candidate(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val rut: String = "",
    val phone: String,
    val role: String, // Profesión / Cargo
    val experience: String,
    val cvUri: String = "",
    val score: Double = 0.0,
    val status: String = "Pendiente",
    val note: String = ""
)

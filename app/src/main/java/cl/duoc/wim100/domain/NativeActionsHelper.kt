package cl.duoc.wim100.domain

import android.content.Context
import android.content.Intent
import android.net.Uri

object NativeActionsHelper {

    fun dialPhoneNumber(context: Context, phoneNumber: String) {
        try {
            if (phoneNumber.isNotBlank()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareCandidateInfo(context: Context, name: String, role: String, score: Double, status: String) {
        try {
            val shareText = """
                📋 *Ficha de Candidata WIM100*
                
                👤 *Nombre:* $name
                💼 *Cargo:* $role
                ⭐ *Puntaje:* ${"%.1f".format(score)} pts
                📌 *Estado:* $status
                
                _Enviado desde la App de Selección WIM100_
            """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Candidata WIM100: $name")
                putExtra(Intent.EXTRA_TEXT, shareText)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val chooser = Intent.createChooser(intent, "Compartir información de la candidata").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

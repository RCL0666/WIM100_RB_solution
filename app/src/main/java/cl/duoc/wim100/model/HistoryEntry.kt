package cl.duoc.wim100.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_entries")
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val text: String
)

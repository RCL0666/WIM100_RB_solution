package cl.duoc.wim100.data

import androidx.room.*
import cl.duoc.wim100.model.Candidate
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Query("SELECT * FROM candidates ORDER BY id DESC")
    fun getAllCandidates(): Flow<List<Candidate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidate(candidate: Candidate)

    @Update
    suspend fun updateCandidate(candidate: Candidate)

    @Query("DELETE FROM candidates WHERE id = :id")
    suspend fun deleteCandidateById(id: Int)

    @Query("SELECT COUNT(*) FROM candidates")
    suspend fun getCandidateCount(): Int
}

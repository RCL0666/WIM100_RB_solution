package cl.duoc.wim100.data

import cl.duoc.wim100.model.Candidate
import cl.duoc.wim100.model.HistoryEntry
import kotlinx.coroutines.flow.Flow

class CandidateRepository(
    private val candidateDao: CandidateDao,
    private val historyDao: HistoryDao
) {
    val allCandidates: Flow<List<Candidate>> = candidateDao.getAllCandidates()
    val allHistory: Flow<List<HistoryEntry>> = historyDao.getAllHistory()

    suspend fun insertCandidate(candidate: Candidate) {
        candidateDao.insertCandidate(candidate)
    }

    suspend fun updateCandidate(candidate: Candidate) {
        candidateDao.updateCandidate(candidate)
    }

    suspend fun deleteCandidate(id: Int) {
        candidateDao.deleteCandidateById(id)
    }

    suspend fun addHistory(entry: HistoryEntry) {
        historyDao.insertHistory(entry)
    }

    suspend fun getCandidateCount(): Int {
        return candidateDao.getCandidateCount()
    }
}

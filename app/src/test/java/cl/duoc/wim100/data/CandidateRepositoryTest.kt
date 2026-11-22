package cl.duoc.wim100.data

import cl.duoc.wim100.model.Candidate
import cl.duoc.wim100.model.HistoryEntry
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CandidateRepositoryTest {

    private val candidateDao = mockk<CandidateDao>(relaxed = true)
    private val historyDao = mockk<HistoryDao>(relaxed = true)

    @Test
    fun `allCandidates emite lista de candidatos correctamente`() = runBlocking {
        val mockCandidates = listOf(
            Candidate(id = 1, name = "Camila Rojas", email = "camila@mail.com", rut = "18.234.567-9", phone = "+56911112222", role = "Desarrolladora Android", experience = "2 años"),
            Candidate(id = 2, name = "Valentina Soto", email = "valentina@mail.com", rut = "19.876.543-0", phone = "+56933334444", role = "Analista QA", experience = "3 años")
        )
        coEvery { candidateDao.getAllCandidates() } returns flowOf(mockCandidates)

        val repository = CandidateRepository(candidateDao, historyDao)
        val result = repository.allCandidates.first()

        assertEquals(2, result.size)
        assertEquals("Camila Rojas", result[0].name)
    }

    @Test
    fun `insertCandidate llama al DAO correspondiente`() = runBlocking {
        val repository = CandidateRepository(candidateDao, historyDao)
        val candidate = Candidate(name = "Fernanda Díaz", email = "fernanda@mail.com", rut = "17.654.321-0", phone = "+56955556666", role = "UI/UX", experience = "1 año")

        repository.insertCandidate(candidate)

        coVerify(exactly = 1) { candidateDao.insertCandidate(candidate) }
    }

    @Test
    fun `deleteCandidate llama al DAO con la ID indicada`() = runBlocking {
        val repository = CandidateRepository(candidateDao, historyDao)
        repository.deleteCandidate(5)

        coVerify(exactly = 1) { candidateDao.deleteCandidateById(5) }
    }

    @Test
    fun `addHistory inserta una nueva entrada en el historial`() = runBlocking {
        val repository = CandidateRepository(candidateDao, historyDao)
        val entry = HistoryEntry(date = "26/10/2026", text = "Prueba unitaria ejecutada")

        repository.addHistory(entry)

        coVerify(exactly = 1) { historyDao.insertHistory(entry) }
    }
}

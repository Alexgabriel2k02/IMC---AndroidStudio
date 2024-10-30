import androidx.room.*

@Dao
interface ImcDao {

    // Create
    @Insert
    suspend fun insert(imcData: ImcData)

    // Read (buscando todos os registros)
    @Query("SELECT * FROM imc_data")
    suspend fun getAll(): List<ImcData>

    // Read (buscando um registro específico por ID)
    @Query("SELECT * FROM imc_data WHERE id = :id")
    suspend fun getById(id: Int): ImcData?

    // Update
    @Update
    suspend fun update(imcData: ImcData)

    // Delete
    @Delete
    suspend fun delete(imcData: ImcData)
}
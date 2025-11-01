package id.firobusiness.propolisstocklite.core.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.InstallIn
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import androidx.room.Room

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val sku: String,
    val name: String,
    val variant: String?,
    val unitPrice: Long,
    val unitCost: Long?,
    val stockQty: Int,
    val minStock: Int,
    val isActive: Boolean = true
)

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun observeAll(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ProductEntity)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Query("UPDATE products SET stockQty = stockQty + :delta WHERE id = :id AND (stockQty + :delta) >= 0")
    suspend fun adjustStock(id: String, delta: Int): Int
}

@Database(entities = [ProductEntity::class], version = 1)
abstract class AppDb: RoomDatabase() {
    abstract fun products(): ProductDao
}

class Repository(private val dao: ProductDao, private val ctx: Context) {
    fun inventory(): Flow<List<ProductEntity>> = dao.observeAll()

    fun getProductsFlow(): Flow<List<ProductEntity>> = dao.observeAll()

    suspend fun seedIfEmpty(seed: List<ProductEntity>) {
        if (dao.count() == 0) dao.upsertAll(seed)
    }

    suspend fun addProduct(product: ProductEntity) {
        dao.upsert(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        dao.upsert(product)
    }

    suspend fun adjustStock(id: String, delta: Int): Boolean =
        dao.adjustStock(id, delta) > 0
}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides @Singleton
    fun db(@ApplicationContext ctx: Context): AppDb =
        Room.databaseBuilder(ctx, AppDb::class.java, "propolis.db").build()

    @Provides @Singleton
    fun dao(db: AppDb): ProductDao = db.products()

    @Provides @Singleton
    fun repo(dao: ProductDao, @ApplicationContext ctx: Context) = Repository(dao, ctx)
}

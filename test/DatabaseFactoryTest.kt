import com.lab.clean.ktor.data.DatabaseFactory
import com.lab.clean.ktor.data.UsersTable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.assertTrue

class DatabaseFactoryTest {
    @Test
    fun connect() {
        DatabaseFactory.initialize()
        transaction {
            UsersTable.selectAll().count()
        }
        assertTrue(true)
    }
}

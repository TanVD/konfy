import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import io.mockk.mockk
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import tanvd.konfy.ssm.SsmProvider
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class SsmProviderTest {

    @Test
    fun shouldPrintCorrectLogMessage() {
        val oldOut = System.out
        val konfyLogKeysParam = "KONFY_LOG_KEYS"
        try {
            //given
            val key = "testKey"
            System.setProperty(konfyLogKeysParam, key)
            val logMessage = "Fetching parameter with key: $key \n Stack trace:"
            val management: AWSSimpleSystemsManagement = mockk(relaxed = true)
            val provider = SsmProvider(null, ".", management)

            val consoleOutput = ByteArrayOutputStream()
            val ps = PrintStream(consoleOutput)
            System.setOut(ps)

            //when
            provider.fetch<String>(key, String::class.java)
            assertTrue(consoleOutput.toString().contains(logMessage))

            //then
        } finally {
            System.clearProperty(konfyLogKeysParam)
            System.setOut(oldOut)
        }
    }

}

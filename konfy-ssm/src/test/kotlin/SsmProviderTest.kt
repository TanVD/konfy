import io.mockk.mockk
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import software.amazon.awssdk.services.ssm.SsmClient
import tanvd.konfy.ssm.SsmProvider
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class SsmProviderTest {

    @Test
    fun shouldPrintCorrectLogMessage() {
        val oldOut = System.out
        val konfyLogKeysParam = "KONFY_LOG_KEYS"
        try {
            //setup
            val consoleOutput = ByteArrayOutputStream()
            val ps = PrintStream(consoleOutput)
            System.setOut(ps)

            //given
            val key = "testKey"
            System.setProperty(konfyLogKeysParam, key)
            val logMessage = "Fetching parameter with key: $key \n Stack trace:"
            val ssmClient: SsmClient = mockk(relaxed = true)
            val provider = SsmProvider(null, ".", ssmClient)

            //when
            provider.fetch<String>(key, String::class.java)

            //then
            assertTrue(consoleOutput.toString().contains(logMessage))
        } finally {
            //cleanup
            System.clearProperty(konfyLogKeysParam)
            System.setOut(oldOut)
        }
    }

}

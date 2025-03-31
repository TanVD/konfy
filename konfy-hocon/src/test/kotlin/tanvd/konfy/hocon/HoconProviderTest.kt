package tanvd.konfy.hocon

import com.typesafe.config.ConfigFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class HoconProviderTest {
    private val configProvider
        get() = HoconProvider(ConfigFactory.parseFile(File("src/test/resources/config.conf")))

    @Test
    fun get_keyExists_gotValue() {
        val value: String = configProvider.get("first_test")
        assertThat(value).isEqualTo("test-pass")
    }

    @Test
    fun tryGet_keyDoesNotExist_noValue() {
        val value: String? = configProvider.tryGet("third_test")
        assertThat(value).isNull()
    }

    @Test
    fun get_keyInTable_gotValue() {
        val value: String = configProvider.get("section.fourth_test")
        assertThat(value).isEqualTo("test-pass")
    }

    @Test
    fun get_arrayValue_gotValue() {
        val value: Array<String> = configProvider.get("section.arena-letters")
        assertThat(value).containsAll(listOf("All", "King", "Edwards", "Horses", "Can", "Move", "Bloody", "Fast"))
    }

    @Test
    fun get_nestedArrayValue_gotValue() {
        val value: Array<Array<String>> = configProvider.get("section.array-in-array")
        assertThat(value).isDeepEqualTo(arrayOf(arrayOf("123"), arrayOf("456")))
    }

    @Test
    fun get_nestedTable_gotValue() {
        val value: String = configProvider.get("section.nested.best-bike-ever")
        assertThat(value).isEqualTo("KTM Duke 390")
    }
}

package tanvd.konfy.properties

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class PropertiesConfigTest {
    private val configProvider
        get() = PropertiesProvider.from(File("src/test/resources/config.properties").readText())

    @Test
    fun get_keyExists_gotValue() {
        val value: String = configProvider.get("first_test")
        assertThat(value).isEqualTo("test-pass")
    }
    
    @Test
    fun get_keyExists_gotIntValue() {
        val value: Int = configProvider.get("second_test")
        assertThat(value).isEqualTo(42)
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
    fun get_valueWithSpaces_gotValue() {
        val value: String = configProvider.get("section.nested.best-bike-ever")
        assertThat(value).isEqualTo("KTM Duke 390")
    }
}

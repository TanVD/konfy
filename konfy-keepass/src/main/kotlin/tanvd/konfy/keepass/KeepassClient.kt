package tanvd.konfy.keepass

import org.linguafranca.pwdb.kdbx.KdbxCreds
import org.linguafranca.pwdb.kdbx.dom.DomDatabaseWrapper
import java.io.File

internal class KeepassClient(databaseFile: File, masterPassword: String) {
    private lateinit var database: DomDatabaseWrapper

    init {
        val credentials = KdbxCreds(masterPassword.toByteArray())
        if (databaseFile.exists()) database =  DomDatabaseWrapper.load(credentials, databaseFile.inputStream())
    }

    fun get(title: String): String? {
        return database.findEntries { it.title == title }.firstOrNull()?.password
    }
}

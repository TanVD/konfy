package tanvd.konfy.keepass

import de.slackspace.openkeepass.KeePassDatabase
import de.slackspace.openkeepass.domain.KeePassFile
import java.io.File

internal class KeepassClient(databaseFile: File, masterPassword: String) {
    private lateinit var database: KeePassFile

    init {
        if (databaseFile.exists()) database = KeePassDatabase.getInstance(databaseFile).openDatabase(masterPassword)
    }

    fun get(title: String): String? {
        return database.getEntryByTitle(title)?.let { it.password }
    }
}

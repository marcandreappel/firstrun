package `fun`.appel.library

import android.content.Context
import android.content.SharedPreferences

/**
 * @package     `fun`.appel.library
 * @project     FirstRun
 * @author      Marc-André Appel <marc-andre@appel.fun>
 * @license     http://opensource.org/licenses/MIT MIT
 * @created     14/06/2018
 */

class FirstRun(private val context: Context) {

    private var lastVersion: Int
    private var thisVersion: Int
    private var appSettings: SharedPreferences

    init {
        appSettings = context
                .getSharedPreferences(FIRST_RUN_SETTINGS, Context.MODE_PRIVATE)
        thisVersion = packageCode()
        lastVersion = settingsCode()
    }

    fun status(): FirstRunStatus {
        val status = when {
            lastVersion == -1 ->
                FirstRunStatus.FIRST_RUN_INSTALL
            lastVersion < thisVersion ->
                FirstRunStatus.FIRST_RUN_VERSION
            else -> FirstRunStatus.NORMAL_RUN
        }

        if (status != FirstRunStatus.NORMAL_RUN) {
            appSettings
                    .edit()
                    .putInt(FIRST_RUN_LAST_APPLICATION_VERSION, thisVersion)
                    .apply()
        }
        return status
    }

    private fun packageCode(): Int {
        val name = context.packageName

        val appPackage = context
                .packageManager
                .getPackageInfo(name, 0)
        return appPackage.versionCode
    }

    private fun settingsCode(): Int {
        if (!appSettings.contains(FIRST_RUN_LAST_APPLICATION_VERSION)) {
            appSettings
                    .edit()
                    .putInt(FIRST_RUN_LAST_APPLICATION_VERSION, -1)
                    .apply()
        }
        return appSettings.getInt(FIRST_RUN_LAST_APPLICATION_VERSION, -1)
    }

    companion object {
        const val FIRST_RUN_LAST_APPLICATION_VERSION = "last_application_version"
        const val FIRST_RUN_SETTINGS = "first_run_settings"
    }
}

enum class FirstRunStatus {
    FIRST_RUN_INSTALL,
    FIRST_RUN_VERSION,
    NORMAL_RUN
}
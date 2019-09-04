package `fun`.appel.firstrun

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo

/**
 * @package     fun.appel.firstrun
 * @project     FirstRun
 * @author      Marc-André Appel <marc-andre@appel.fun>
 * @license     http://opensource.org/licenses/MIT MIT
 */

class FirstRun(context: Context) {

    private var appSettings: SharedPreferences = context
            .getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    private var packageName: String = context.packageName
    private val packageInfo: PackageInfo = context.packageManager.getPackageInfo(packageName, 0)

    private var lastCode: Int = settingsCode()
    private var lastVersion: String = settingsVersion()

    private val thisVersion: String = packageInfo.versionName
    @Suppress("DEPRECATION")
    private val thisCode: Int = packageInfo.versionCode

    /**
     * Treat
     */
    fun status(): RunType {
        val status = when {
            lastCode == -1 ->
                INSTALL
            lastCode < thisCode -> {
                val updateType = compare(thisVersion, lastVersion)
                if (updateType != -1) {
                    when (updateType) {
                        0 -> MAJOR
                        1 -> MINOR
                        2 -> PATCH
                        else -> UPDATE
                    }
                } else {
                    UPDATE
                }
            }
            else ->
                NORMAL
        }
        if (status != NORMAL) {
            appSettings
                    .edit()
                    .putInt(CODE, thisCode)
                    .putString(VERSION, thisVersion)
                    .apply()
        }
        return status
    }

    private fun settingsCode(): Int {
        if (!appSettings.contains(CODE)) {
            appSettings
                    .edit()
                    .putInt(CODE, -1)
                    .apply()
        }
        return appSettings.getInt(CODE, -1)
    }

    private fun settingsVersion(): String {
        if (!appSettings.contains(VERSION)) {
            appSettings
                    .edit()
                    .putString(VERSION, "0.0.0")
                    .apply()
        }
        return appSettings.getString(VERSION, "0.0.0")!!
    }

    /**
     * Returns major, minor or patch value
     *
     * @param left  thisVersion
     * @param right lastVersion
     *
     * @return Int
     */
    private fun compare(left: String, right: String): Int {
        if (left == right) {
            return -1
        }

        var leftStart = 0
        var rightStart = 0
        var result: Int
        var count = 0

        do {
            val leftEnd = left.indexOf('.', leftStart)
            val rightEnd = right.indexOf('.', rightStart)

            val leftValue = Integer.parseInt(
                    if (leftEnd < 0) {
                        left.substring(leftStart)
                    } else {
                        left.substring(leftStart, leftEnd)
                    })

            val rightValue = Integer.parseInt(
                    if (rightEnd < 0) {
                        right.substring(rightStart)
                    } else {
                        right.substring(rightStart, rightEnd)
                    })

            result = leftValue.compareTo(rightValue)
            if (result != 0) {
                return count
            }
            leftStart = leftEnd + 1
            rightStart = rightEnd + 1

            count++
        } while (result == 0 && leftStart > 0 && rightStart > 0)

        // No difference found
        return -1
    }

    companion object {
        const val CODE = "versionCode"
        const val VERSION = "versionName"
        const val SETTINGS = "firstRunSettings"

        // Run types
        val INSTALL = RunType.INSTALL
        val NORMAL = RunType.NORMAL
        // Code update only
        val UPDATE = RunType.UPDATE
        // Semantic update types
        val MAJOR = RunType.MAJOR
        val MINOR = RunType.MINOR
        val PATCH = RunType.PATCH
    }
}

enum class RunType {
    NORMAL,
    INSTALL,
    UPDATE,
    MAJOR,
    MINOR,
    PATCH
}

package `fun`.appel.firstrun

import `fun`.appel.library.FirstRun
import `fun`.appel.library.FirstRunStatus
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstRunStatus = FirstRun(this).status()

        when (firstRunStatus) {
            FirstRunStatus.FIRST_RUN_INSTALL -> {
                primary_text.text = resources.getText(R.string.text_first_run_install)
                secondary_text.text = resources.getText(R.string.text_first_run_install_sub)
            }
            FirstRunStatus.FIRST_RUN_VERSION -> {
                primary_text.text = resources.getText(R.string.text_first_run_version)
                secondary_text.text = resources.getText(R.string.text_first_run_version_sub)
            }
            else -> {
                primary_text.text = resources.getText(R.string.text_first_run_normal)
                secondary_text.visibility = View.GONE
            }
        }

        val lastVersion = getSharedPreferences(FirstRun.FIRST_RUN_SETTINGS, Context.MODE_PRIVATE)
                .getInt(FirstRun.FIRST_RUN_LAST_APPLICATION_VERSION, 999)
        LAST_VERSION.text = lastVersion.toString()

        val thisVersion = packageManager
                .getPackageInfo(packageName, 0)
                .versionCode
        THIS_VERSION.text = thisVersion.toString()
    }
}

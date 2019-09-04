package `fun`.appel.firstrun

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (val runType = FirstRun(this).status()) {
            RunType.INSTALL -> {
                primary_text.text = resources.getText(R.string.text_first_run_install)
                secondary_text.text = resources.getText(R.string.text_first_run_install_subtitle)
            }
            RunType.NORMAL -> {
                primary_text.text = resources.getText(R.string.text_first_run_normal)
                secondary_text.visibility = View.GONE
            }
            else -> {
                val append = when (runType) {
                    RunType.MAJOR -> " Major update."
                    RunType.MINOR -> " Minor update."
                    RunType.PATCH -> " Patch update."
                    else -> " Code update only."
                }
                val text = resources.getText(R.string.text_first_run_update)
                primary_text.text = "$text $append"
                secondary_text.text = resources.getText(R.string.text_first_run_update_subtitle)
            }
        }

        val version = getSharedPreferences(FirstRun.SETTINGS, Context.MODE_PRIVATE)
                .getString(FirstRun.VERSION, "0.0.0")
        VERSION.text = version

        @Suppress("DEPRECATION") val code = packageManager
                .getPackageInfo(packageName, 0)
                .versionCode
        CODE.text = code.toString()
    }
}

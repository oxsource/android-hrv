package pizzk.android.hrvquest.common.view.widget

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import pizzk.android.hrvquest.R

class UIProgressDialog(context: Context) : AlertDialog(context, R.style.UIProgressDialog) {
    private var tvContent: TextView? = null
    private var message: CharSequence? = null
    private val defaultMessage: String = context.resources.getString(R.string.please_waiting)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_progress_dialog)
        tvContent = findViewById(R.id.tvContent)
        tvContent!!.text = if (TextUtils.isEmpty(message)) defaultMessage else message
    }

    override fun setMessage(message: CharSequence) {
        var content = message
        if (!TextUtils.isEmpty(content) && content.length > 16) {
            content = defaultMessage
        }
        this.message = content
        tvContent?.text = content
    }

    override fun show() {
        val context: Context = context ?: return
        if (context is Activity && context.isFinishing) {
            return
        }
        super.show()
    }

    companion object {
        fun build(context: Context): UIProgressDialog {
            return UIProgressDialog(context)
        }
    }
}
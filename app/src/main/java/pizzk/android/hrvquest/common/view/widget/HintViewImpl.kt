package pizzk.android.hrvquest.common.view.widget

import android.app.Activity
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import pizzk.android.hrvquest.R

interface HintView {

    fun showToast(content: CharSequence?, center: Boolean = false, duration: Int = 0)

    fun showSnack(
        view: View, content: CharSequence,
        action: CharSequence = "", listener: (View) -> Unit = {}
    )

    fun hideSnack()

    fun showAlert(
        title: CharSequence, content: CharSequence, cancelAble: Boolean = false,
        positiveText: CharSequence = "", positiveClick: () -> Unit = {},
        negativeText: CharSequence = "", negativeClick: () -> Unit = {}
    )

    fun hideAlert()

    fun showLoading(content: CharSequence = "", cancelAble: Boolean = false)

    fun hideLoading()

    fun isLoading(): Boolean

    fun onResume()

    fun onPause()
}

class HintViewImpl(private val activity: Activity) : HintView {
    private var snack: Snackbar? = null
    private var alert: AlertDialog? = null
    private var loading: AlertDialog? = null
    //是否可与界面交互标志
    private var interactive: Boolean = false
    private var loadingParams: HintViewImpl.LoadingParams = HintViewImpl.LoadingParams()

    override fun showToast(content: CharSequence?, center: Boolean, duration: Int) {
        val text: CharSequence = content ?: return
        val ms: Int = if (duration in Toast.LENGTH_SHORT..Toast.LENGTH_LONG) duration else Toast.LENGTH_SHORT
        val toast: Toast = Toast.makeText(activity, text, ms)
        if (center) {
            toast.setGravity(Gravity.CENTER, 0, 0)
        }
        toast.show()
    }

    override fun showSnack(view: View, content: CharSequence, action: CharSequence, listener: (View) -> Unit) {
        hideSnack()
        val bar: Snackbar = Snackbar.make(view, content, Snackbar.LENGTH_SHORT)
        if (!TextUtils.isEmpty(action)) {
            bar.setAction(action) { v -> listener(v) }
        }
        bar.show()
        snack = bar
    }

    override fun hideSnack() {
        snack?.let { if (it.isShown) it.dismiss() }
        snack = null
    }

    override fun showAlert(
        title: CharSequence, content: CharSequence, cancelAble: Boolean,
        positiveText: CharSequence, positiveClick: () -> Unit,
        negativeText: CharSequence, negativeClick: () -> Unit
    ) {
        hideAlert()
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder = builder.setCancelable(cancelAble)
        builder = builder.setTitle(title)
        builder = builder.setMessage(content)
        if (!TextUtils.isEmpty(positiveText)) {
            builder = builder.setPositiveButton(positiveText) { _, _ ->
                hideAlert()
                positiveClick()
            }
        }
        if (!TextUtils.isEmpty(negativeText)) {
            builder = builder.setNegativeButton(negativeText) { _, _ ->
                hideAlert()
                negativeClick()
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        alert = dialog
    }

    override fun hideAlert() {
        alert?.let { if (it.isShowing) it.dismiss() }
        alert = null
    }

    override fun showLoading(content: CharSequence, cancelAble: Boolean) {
        //界面可交互时才会加载，否则延迟到resume中加载
        if (interactive) {
            hideLoading()
            val progress: AlertDialog = loading ?: UIProgressDialog.build(activity)
            progress.setMessage(if (content.isEmpty()) activity.getString(R.string.please_waiting) else content)
            progress.setCancelable(cancelAble)
            progress.show()
            loading = progress
            return
        }
        loadingParams.shown = true
        loadingParams.content = content
        loadingParams.cancel = cancelAble
    }

    override fun hideLoading() {
        loadingParams.shown = false
        loadingParams.content = ""
        loadingParams.cancel = false
        loading?.let { if (it.isShowing) it.dismiss() }
    }

    override fun isLoading(): Boolean = loading?.isShowing ?: false

    override fun onResume() {
        interactive = true
        if (loadingParams.shown) {
            showLoading(loadingParams.content, loadingParams.cancel)
        }
    }

    override fun onPause() {
        interactive = false
        hideLoading()
        hideSnack()
        hideAlert()
    }

    data class LoadingParams(var shown: Boolean = false, var content: CharSequence = "", var cancel: Boolean = false)
}


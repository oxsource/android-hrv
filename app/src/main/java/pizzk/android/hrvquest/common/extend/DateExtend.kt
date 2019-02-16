package pizzk.android.hrvquest.common.extend

import java.util.*

fun Date?.expire(standard: Date?): Boolean {
    val date: Date = this ?: return true
    val base: Date = standard ?: return true
    return base.before(date)
}
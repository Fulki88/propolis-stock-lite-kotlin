package id.firobusiness.propolisstocklite.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// Density extensions
@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}

@Composable
fun Int.toDp(): Dp {
    return with(LocalDensity.current) { this@toDp.toDp() }
}

// String extensions
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPhone(): Boolean {
    return android.util.Patterns.PHONE.matcher(this).matches()
}

fun String.capitalizeFirst(): String {
    return if (isNotEmpty()) {
        first().uppercaseChar() + substring(1).lowercase()
    } else {
        this
    }
}

fun String.toTitleCase(): String {
    return split(" ").joinToString(" ") { it.capitalizeFirst() }
}

// Number extensions
fun Double.formatCurrency(currency: String = AppConstants.DEFAULT_CURRENCY): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return when (currency) {
        "IDR" -> "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(this)}"
        "USD" -> format.format(this)
        else -> "$currency $this"
    }
}

fun Int.formatNumber(): String {
    return NumberFormat.getNumberInstance(Locale("id", "ID")).format(this)
}

fun Double.formatDecimal(decimalPlaces: Int = 2): String {
    return "%.${decimalPlaces}f".format(this)
}

// Date extensions
fun Date.formatToString(pattern: String = AppConstants.DATE_FORMAT_DISPLAY): String {
    val formatter = SimpleDateFormat(pattern, Locale("id", "ID"))
    return formatter.format(this)
}

fun String.parseToDate(pattern: String = AppConstants.DATE_FORMAT_API): Date? {
    return try {
        val formatter = SimpleDateFormat(pattern, Locale("id", "ID"))
        formatter.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun Long.toDate(): Date {
    return Date(this)
}

fun Date.isToday(): Boolean {
    val today = Calendar.getInstance()
    val dateCalendar = Calendar.getInstance().apply { time = this@isToday }
    
    return today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR)
}

fun Date.isYesterday(): Boolean {
    val yesterday = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }
    val dateCalendar = Calendar.getInstance().apply { time = this@isYesterday }
    
    return yesterday.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR)
}

fun Date.isTomorrow(): Boolean {
    val tomorrow = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
    }
    val dateCalendar = Calendar.getInstance().apply { time = this@isTomorrow }
    
    return tomorrow.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
            tomorrow.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR)
}

// Collection extensions
fun <T> List<T>.isNotNullOrEmpty(): Boolean {
    return !isNullOrEmpty()
}

fun <T> List<T>.getOrEmpty(index: Int): T? {
    return if (index in 0 until size) this[index] else null
}

// Validation extensions
fun String.isNotBlankOrEmpty(): Boolean {
    return isNotBlank() && isNotEmpty()
}

fun String.hasMinLength(minLength: Int): Boolean {
    return length >= minLength
}

fun String.hasMaxLength(maxLength: Int): Boolean {
    return length <= maxLength
}

fun Double.isValidPrice(): Boolean {
    return this >= AppConstants.MIN_PRICE && this <= AppConstants.MAX_PRICE
}

fun Int.isValidQuantity(): Boolean {
    return this >= AppConstants.MIN_QUANTITY && this <= AppConstants.MAX_QUANTITY
}

// Utility functions
fun currentTimestamp(): Long = System.currentTimeMillis()

fun currentDate(): Date = Date()

fun generateId(): String = UUID.randomUUID().toString()

fun safeCall(action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
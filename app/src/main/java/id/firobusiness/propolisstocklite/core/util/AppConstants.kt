package id.firobusiness.propolisstocklite.core.util

object AppConstants {
    
    // App Information
    const val APP_NAME = "Propolis Stock Lite"
    const val APP_VERSION = "1.0.0"
    
    // Animation Durations
    const val ANIMATION_DURATION_SHORT = 150
    const val ANIMATION_DURATION_MEDIUM = 300
    const val ANIMATION_DURATION_LONG = 500
    
    // Database
    const val DATABASE_NAME = "propolis_stock_database"
    const val DATABASE_VERSION = 1
    
    // Shared Preferences
    const val PREFS_NAME = "propolis_stock_prefs"
    const val PREF_FIRST_LAUNCH = "first_launch"
    const val PREF_THEME_MODE = "theme_mode"
    
    // Theme Modes
    const val THEME_LIGHT = 0
    const val THEME_DARK = 1
    const val THEME_SYSTEM = 2
    
    // Screen Routes
    object Routes {
        const val HOME = "home"
        const val INVENTORY = "inventory"
        const val CREATE_STOCK = "create_stock"
        const val EDIT_STOCK = "edit_stock"
        const val BUYER_PURCHASE = "buyer_purchase"
        const val SETTINGS = "settings"
        const val ABOUT = "about"
    }
    
    // Stock Categories
    object StockCategories {
        const val PROPOLIS_RAW = "propolis_raw"
        const val PROPOLIS_EXTRACT = "propolis_extract"
        const val PROPOLIS_CAPSULES = "propolis_capsules"
        const val PROPOLIS_LIQUID = "propolis_liquid"
        const val PROPOLIS_POWDER = "propolis_powder"
        const val OTHER = "other"
    }
    
    // Units of Measurement
    object Units {
        const val GRAM = "gram"
        const val KILOGRAM = "kilogram"
        const val MILLILITER = "milliliter"
        const val LITER = "liter"
        const val PIECE = "piece"
        const val BOTTLE = "bottle"
        const val PACK = "pack"
    }
    
    // Date Formats
    const val DATE_FORMAT_DISPLAY = "dd MMM yyyy"
    const val DATE_FORMAT_API = "yyyy-MM-dd"
    const val DATETIME_FORMAT_DISPLAY = "dd MMM yyyy, HH:mm"
    const val DATETIME_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss"
    
    // Validation
    const val MIN_STOCK_NAME_LENGTH = 3
    const val MAX_STOCK_NAME_LENGTH = 50
    const val MIN_DESCRIPTION_LENGTH = 10
    const val MAX_DESCRIPTION_LENGTH = 200
    const val MIN_PRICE = 0.01
    const val MAX_PRICE = 999999.99
    const val MIN_QUANTITY = 0
    const val MAX_QUANTITY = 999999
    
    // Default Values
    const val DEFAULT_CURRENCY = "IDR"
    const val DEFAULT_LANGUAGE = "id"
    const val DEFAULT_COUNTRY = "ID"
    
    // Error Messages
    object ErrorMessages {
        const val NETWORK_ERROR = "Network connection error"
        const val GENERIC_ERROR = "Something went wrong"
        const val VALIDATION_ERROR = "Please check your input"
        const val EMPTY_FIELD = "This field cannot be empty"
        const val INVALID_EMAIL = "Please enter a valid email"
        const val INVALID_PHONE = "Please enter a valid phone number"
    }
    
    // Success Messages
    object SuccessMessages {
        const val STOCK_CREATED = "Stock created successfully"
        const val STOCK_UPDATED = "Stock updated successfully"
        const val STOCK_DELETED = "Stock deleted successfully"
        const val PURCHASE_COMPLETED = "Purchase completed successfully"
    }
    
    // Intent Extra Keys
    object IntentExtras {
        const val STOCK_ID = "stock_id"
        const val PURCHASE_ID = "purchase_id"
        const val CATEGORY = "category"
        const val EDIT_MODE = "edit_mode"
    }
    
    // Request Codes
    object RequestCodes {
        const val CAMERA_REQUEST = 1001
        const val GALLERY_REQUEST = 1002
        const val PERMISSION_REQUEST = 1003
    }
    
    // File Operations
    const val MAX_IMAGE_SIZE_MB = 5
    const val IMAGE_QUALITY = 85
    const val EXPORT_FILE_PREFIX = "propolis_stock_export"
    
    // Notification
    object Notifications {
        const val CHANNEL_ID = "propolis_stock_channel"
        const val CHANNEL_NAME = "Propolis Stock Notifications"
        const val LOW_STOCK_NOTIFICATION_ID = 1001
        const val EXPIRY_NOTIFICATION_ID = 1002
    }
}
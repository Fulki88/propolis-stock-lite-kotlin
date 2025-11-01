package id.firobusiness.propolisstocklite.core.common

import java.util.UUID

object Ids {
    fun newId(): String = UUID.randomUUID().toString()
}

sealed class AppError(msg: String): Exception(msg) {
    class Validation(message: String): AppError(message)
    class NotFound(message: String): AppError(message)
}

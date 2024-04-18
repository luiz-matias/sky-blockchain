package model

import com.google.gson.Gson
import java.math.BigDecimal

data class Transaction(
    val amount: BigDecimal = BigDecimal.ZERO,
    val payer: String,
    val payee: String,
    var signature: String? = null,
) {
    override fun toString(): String {
        return Gson().toJson(this.copy(signature = null))
    }
}

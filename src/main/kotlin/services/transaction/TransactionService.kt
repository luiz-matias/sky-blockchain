package services.transaction

import model.Transaction
import java.math.BigDecimal

interface TransactionService {

    fun createTransaction(
        from: String,
        to: String,
        amount: BigDecimal,
        payerPrivateKey: String,
    ): Transaction

    fun validateTransaction(transaction: Transaction): Boolean

}
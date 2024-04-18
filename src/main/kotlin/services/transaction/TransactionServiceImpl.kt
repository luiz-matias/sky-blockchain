package services.transaction

import model.Transaction
import util.signRSAWithSHA256
import util.verifySignature
import java.math.BigDecimal

class TransactionServiceImpl : TransactionService {

    override fun createTransaction(
        from: String,
        to: String,
        amount: BigDecimal,
        payerPrivateKey: String,
    ): Transaction {
        val transaction = Transaction(
            amount,
            from,
            to,
        )
        val transactionJson = transaction.toString()
        val signedTransaction = transactionJson.signRSAWithSHA256(payerPrivateKey)
        transaction.signature = signedTransaction
        return transaction
    }

    override fun validateTransaction(transaction: Transaction): Boolean {
        if (transaction.signature == null) {
            return false
        }

        return transaction.toString().verifySignature(transaction.payer, transaction.signature!!)
    }

}
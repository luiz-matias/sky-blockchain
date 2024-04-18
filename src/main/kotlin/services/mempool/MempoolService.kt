package services.mempool

import model.Transaction

interface MempoolService {

    fun addTransactionToMempool(transaction: Transaction)

    fun sendMempoolTransactionsToBlockchain()

}
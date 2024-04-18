package services.mempool

import model.Transaction
import services.blockchain.BlockchainService

class MempoolServiceImpl(private val blockchainService: BlockchainService) : MempoolService {

    private val pendingTransactions: MutableList<Transaction> = mutableListOf()

    override fun addTransactionToMempool(transaction: Transaction) {
        pendingTransactions.add(transaction)
        println("Transaction added to mempool")
    }

    override fun sendMempoolTransactionsToBlockchain() {
        println("Sending mempool transactions to blockchain...")
        val transactions = mutableListOf<Transaction>()
        transactions.addAll(pendingTransactions)
        var block = blockchainService.createBlock(transactions)
        block = blockchainService.mineBlock(block)
        blockchainService.sendBlockToBlockchain(block)
        pendingTransactions.clear()
    }
}
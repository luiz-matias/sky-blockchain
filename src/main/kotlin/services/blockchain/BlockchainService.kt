package services.blockchain

import model.Transaction
import model.block.Block

interface BlockchainService {

    fun createBlock(transactions: List<Transaction>): Block

    fun mineBlock(block: Block): Block

    fun sendBlockToBlockchain(block: Block)

    fun getCompleteBlockchain(): List<Block>

}
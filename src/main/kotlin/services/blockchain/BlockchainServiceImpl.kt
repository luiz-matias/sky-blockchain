package services.blockchain

import datasources.BlockchainDataSource
import model.Transaction
import model.block.Block
import model.block.BlockData
import model.block.BlockHeader
import util.toHexString
import util.toSHA256
import java.util.*

class BlockchainServiceImpl(private val blockchainDataSource: BlockchainDataSource) : BlockchainService {

    companion object {
        private const val CHAIN_DIFFICULTY = 2
    }

    init {
        if (blockchainDataSource.getLastBlock() == null) {
            createGenesisBlock()
        }
    }

    override fun createBlock(transactions: List<Transaction>): Block {
        val lastBlock = blockchainDataSource.getLastBlock()
        return Block(
            BlockHeader(
                0,
                null,
            ),
            BlockData(
                UUID.randomUUID(),
                Date().time,
                lastBlock?.blockHeader?.hash,
                transactions,
            ),
        )
    }

    override fun mineBlock(block: Block): Block {
        var nonce = block.blockHeader.nonce
        while (true) {
            val hashString = generateBlockHash(block, nonce)
            if (isValidHash(hashString)) {
                block.blockHeader.hash = hashString
                block.blockHeader.nonce = nonce
                return block
            }
            nonce++
        }
    }

    override fun getCompleteBlockchain(): List<Block> {
        return blockchainDataSource.getBlockchain()
    }

    private fun verifyBlock(block: Block): Boolean {
        val lastBlock = blockchainDataSource.getLastBlock()
        if (block.blockData.previousBlockHash != lastBlock?.blockHeader?.hash) {
            println("Invalid block ! Previous hash different from last block hash")
            println("Verified block hash: ${block.blockData.previousBlockHash}")
            println("Previous block hash: ${lastBlock?.blockHeader?.hash}")
            return false
        }

        val hash = generateBlockHash(block, block.blockHeader.nonce)
        if (!isValidHash(hash)) {
            println("Invalid block! Hash has invalid format")
            println("Generated block hash: $hash")
            return false
        }

        return true
    }

    override fun sendBlockToBlockchain(block: Block) {
        if (verifyBlock(block)) {
            blockchainDataSource.addBlock(block)
        }
    }

    private fun createGenesisBlock() {
        println("Creating genesis block...")
        var genesisBlock = createBlock(emptyList())
        genesisBlock = mineBlock(genesisBlock)
        sendBlockToBlockchain(genesisBlock)
    }

    private fun generateBlockHash(block: Block, nonce: Long): String {
        return "${(block.getDataHash())}${nonce}".toSHA256().toHexString()
    }

    private fun isValidHash(hashString: String): Boolean {
        return hashString.startsWith("0".repeat(CHAIN_DIFFICULTY))
    }
}
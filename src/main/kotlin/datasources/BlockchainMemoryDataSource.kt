package datasources

import model.block.Block

class BlockchainMemoryDataSource : BlockchainDataSource {

    private val blockchain = mutableListOf<Block>()

    override fun getLastBlock(): Block? {
        return blockchain.lastOrNull()
    }

    override fun getBlockchain(): List<Block> {
        return blockchain
    }

    override fun addBlock(block: Block) {
        blockchain.add(block)
    }
}
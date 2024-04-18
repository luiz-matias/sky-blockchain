package datasources

import model.block.Block

interface BlockchainDataSource {

    fun getLastBlock(): Block?

    fun getBlockchain(): List<Block>

    fun addBlock(block: Block)

}
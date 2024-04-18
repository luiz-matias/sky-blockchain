package model.block

import com.google.gson.Gson
import util.toHexString
import util.toSHA256

data class Block(
    val blockHeader: BlockHeader,
    val blockData: BlockData,
) {

    fun getDataHash(): String {
        val blockJson = Gson().toJson(this.blockData)
        return blockJson.toSHA256().toHexString()
    }
}

package model.block

import model.Transaction
import java.util.*

data class BlockData(
    val blockUuid: UUID,
    val timestamp: Long,
    var previousBlockHash: String?,
    var transactions: List<Transaction>,
)

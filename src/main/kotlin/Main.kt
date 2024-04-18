import com.google.gson.Gson
import datasources.BlockchainDataSource
import datasources.BlockchainMemoryDataSource
import model.Wallet
import services.blockchain.BlockchainService
import services.blockchain.BlockchainServiceImpl
import services.mempool.MempoolService
import services.mempool.MempoolServiceImpl
import services.transaction.TransactionService
import services.transaction.TransactionServiceImpl
import java.math.BigDecimal

fun main() {
    println("initializing sky-blockchain...")
    //instance of services to sustain the blockchain
    val blockchainService = getBlockchain()
    val mempoolService = getMempoolService(blockchainService)
    val transactionService = getTransactionService()

    //creating wallets
    val firstWallet = Wallet.createWallet()
    val secondWallet = Wallet.createWallet()

    val firstTransaction = transactionService.createTransaction(
        from = firstWallet.publicKey,
        to = secondWallet.publicKey,
        amount = BigDecimal(50),
        payerPrivateKey = firstWallet.privateKey,
    )

    mempoolService.addTransactionToMempool(firstTransaction)

    val secondTransaction = transactionService.createTransaction(
        from = secondWallet.publicKey,
        to = firstWallet.publicKey,
        amount = BigDecimal(25),
        payerPrivateKey = secondWallet.privateKey,
    )

    mempoolService.addTransactionToMempool(secondTransaction)

    mempoolService.sendMempoolTransactionsToBlockchain()

    println("--- Complete blockchain ---")
    val blocks = blockchainService.getCompleteBlockchain()
    println(Gson().toJson(blocks))
}

fun getBlockchain(): BlockchainService {
    return BlockchainServiceImpl(getBlockchainDataSource())
}

fun getBlockchainDataSource(): BlockchainDataSource {
    return BlockchainMemoryDataSource()
}

fun getMempoolService(blockchainService: BlockchainService): MempoolService {
    return MempoolServiceImpl(blockchainService)
}

fun getTransactionService(): TransactionService {
    return TransactionServiceImpl()
}
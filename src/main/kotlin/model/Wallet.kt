package model

import util.generateKeyPair
import util.toHexString

data class Wallet(
    val publicKey: String,
    val privateKey: String,
) {
    companion object {
        fun createWallet(): Wallet {
            val keypair = generateKeyPair()
            return Wallet(
                keypair.public.encoded.toHexString(),
                keypair.private.encoded.toHexString(),
            )
        }
    }
}

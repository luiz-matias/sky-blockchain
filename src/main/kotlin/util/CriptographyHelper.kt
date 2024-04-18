package util

import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

fun String.toSHA256(): ByteArray {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): ByteArray {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
}

fun generateKeyPair(): KeyPair {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
    keyPairGenerator.initialize(2048, SecureRandom(Math.random().toString().toByteArray()))
    return keyPairGenerator.genKeyPair()
}

fun ByteArray.toHexString(): String {
    return this.joinToString("") {
        java.lang.String.format("%02x", it)
    }
}

fun String.decodeHex(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

fun String.convertToPrivateKey(): PrivateKey {
    val keyFactory = KeyFactory.getInstance("RSA")
    return keyFactory.generatePrivate(PKCS8EncodedKeySpec(this.decodeHex()))
}

fun String.convertToPublicKey(): PublicKey {
    val keyFactory = KeyFactory.getInstance("RSA")
    return keyFactory.generatePublic(X509EncodedKeySpec(this.decodeHex()))
}

fun String.signRSAWithSHA256(hexPrivateKey: String): String {
    val privateSignature = Signature.getInstance("SHA256withRSA")
    privateSignature.initSign(hexPrivateKey.convertToPrivateKey())
    privateSignature.update(this.toSHA256())
    return privateSignature.sign().toHexString()
}

fun String.verifySignature(hexPublicKey: String, signature: String): Boolean {
    val content = this
    val signatureVerifier = Signature.getInstance("SHA256withRSA")

    signatureVerifier.initVerify(hexPublicKey.convertToPublicKey())
    signatureVerifier.update(content.toSHA256())
    return signatureVerifier.verify(signature.decodeHex())
}
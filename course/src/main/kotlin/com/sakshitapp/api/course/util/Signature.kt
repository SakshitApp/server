package com.sakshitapp.api.course.util

import java.security.SignatureException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


/**
 * This class defines common routines for generating
 * authentication signatures for Razorpay Webhook requests.
 */
object Signature {
    private const val HMAC_SHA256_ALGORITHM = "HmacSHA256"

    /**
     * Computes RFC 2104-compliant HMAC signature.
     * * @param data
     * The data to be signed.
     * @param key
     * The signing key.
     * @return
     * The Base64-encoded RFC 2104-compliant HMAC signature.
     * @throws
     * java.security.SignatureException when signature generation fails
     */
    @Throws(SignatureException::class)
    fun calculateRFC2104HMAC(data: String, secret: String): String {
        val result: String
        result = try {

            // get an hmac_sha256 key from the raw secret bytes
            val signingKey = SecretKeySpec(secret.toByteArray(), HMAC_SHA256_ALGORITHM)

            // get an hmac_sha256 Mac instance and initialize with the signing key
            val mac = Mac.getInstance(HMAC_SHA256_ALGORITHM)
            mac.init(signingKey)

            // compute the hmac on input data bytes
            val rawHmac = mac.doFinal(data.toByteArray())

            // base64-encode the hmac
            DatatypeConverter.printHexBinary(rawHmac).toLowerCase()
        } catch (e: Exception) {
            throw SignatureException("Failed to generate HMAC : " + e.message)
        }
        return result
    }
}
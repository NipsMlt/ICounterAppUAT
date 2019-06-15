package utility;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author raheel NIPS
 */
public class EncryptDecrpt {
    
    
    // Default Constructor
    public EncryptDecrpt() {
        
    }
   
    /**
     * Algorithm is used For Encryption of the String      *
     * @param sPlainText -> plain text to be encrypted
     *
     * @param sSecretKey -> Key which is used in the encryption
     *
     * @return -> Encrypted Values
     *
     * @throws InvalidKeySpecException
     */
    public static String Encrypt( String sPlainText, String sSecretKey ) throws InvalidKeySpecException {
        try {

            // Initiaize the key byte array
            byte[] keyArray;
            byte[] toEncryptArray = sPlainText.getBytes("UTF-8");

            // Convert the secret key in to MD5 hashing
            java.security.MessageDigest hashMD5 = java.security.MessageDigest.getInstance("MD5");
            keyArray = hashMD5.digest(sSecretKey.getBytes("UTF-8"));

            // Check if the key is less than 16 byte
            if ( keyArray.length == 16 ) {
                byte[] tmpKey = new byte[24];
                System.arraycopy(keyArray, 0, tmpKey, 0, 16);
                System.arraycopy(keyArray, 0, tmpKey, 16, 8);
                keyArray = tmpKey;
            }

            // Make the Cipher here
            Cipher cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
            SecretKeySpec myKey = new SecretKeySpec(keyArray, "DESede");

            // Initialize the Encryption mode
            cipher.init(Cipher.ENCRYPT_MODE, myKey);

            byte[] cipherText = cipher.doFinal(toEncryptArray);
            String encodedCipherText;
            
            // Convert the text into base 64 string
            //encodedCipherText =  DatatypeConverter.printBase64Binary(cipherText);
            encodedCipherText = android.util.Base64.encodeToString(cipherText,android.util.Base64.DEFAULT);
            encodedCipherText = encodedCipherText.substring(0,encodedCipherText.length()-1);
            // Return the base 64 encrypted string
            return encodedCipherText;

        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException oException) {

            return oException.toString();

        }
    }

    /**
     * Algorithm is used for the decryption of the text
     *
     * @param sCipherText -> Cipher Text which is the result of the encrypted
     * Text
     *
     * @param sSecretKey -> Same secret key is used in both encryption and
     * decryption
     *
     * @return -> Decrypted Text
     */
    public static String Decrypt( String sCipherText, String sSecretKey ) {

        try {

            // Initiaize the key byte array
            byte[] keyArray;

            //Convert the byte array into base 64
            byte[] toEncryptArray = Base64.getDecoder().decode( sCipherText );

            // MD5 hashing
            byte[] bytesOfMessage = sSecretKey.getBytes("UTF-8");

            MessageDigest hashMD5 = MessageDigest.getInstance("MD5");
            keyArray = hashMD5.digest(bytesOfMessage);

            keyArray = hashMD5.digest(sSecretKey.getBytes("UTF-8"));
            
            // Check if the key is less than 16 byte
            if (keyArray.length == 16) {
                byte[] tmpKey = new byte[24];
                System.arraycopy(keyArray, 0, tmpKey, 0, 16);
                System.arraycopy(keyArray, 0, tmpKey, 16, 8);
                keyArray = tmpKey;
            }

            // Reset the hash
            hashMD5.reset();

            // Create the DES instance here
            Cipher cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
            SecretKeySpec myKey = new SecretKeySpec(keyArray, "DESede");

            cipher.init(Cipher.DECRYPT_MODE, myKey);

            //Decrption perform here
            byte[] cipherText = cipher.doFinal(toEncryptArray);
            String encodedCipherText;

            // Change the final byte array to string
            encodedCipherText = new String(cipherText, "UTF-8");

            // Return the decrypted string
            return encodedCipherText;

            // Exception are there
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException oException) {

            return oException.toString();
        }
    }
    
    /**
     * To validate the Encryption and Decryption
     * @param text
     * @param key
     * @param compareString
     * @param isEncrypt
     * @return 
     * @throws java.security.spec.InvalidKeySpecException 
     */
    public static boolean validate( String text, String key, String compareString, boolean isEncrypt ) throws InvalidKeySpecException {
        
        // Set to false
        boolean result = false;
        
        // Check if Encryption validation occurs
        if ( isEncrypt ) {
            
            if (compareString.equals(Encrypt(text, key) ) ) {
                   
                result = true;
            }
            
            // Else Decryption validation occurs
        } else {
            
            if (compareString.equals(Decrypt(text, key) ) ) {
               
                result = true;
            }
        }
        
        return result;
    }

}












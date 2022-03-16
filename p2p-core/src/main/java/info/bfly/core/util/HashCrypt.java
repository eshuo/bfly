package info.bfly.core.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class HashCrypt {
    public static String                    SHA                = "SHA";
    public static String                    MD5                = "MD5";
    private final static ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();
    private final static Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();

    public static String getDigestHash(String str) {
        return HashCrypt.getDigestHash(str, "SHA");
    }

    public static String getDigestHash(String str, String hashType) {
        if (str == null) return null;
        return HashCrypt.getDigestHash(str, hashType, null);
    }

    public static String getDigestHash(String str, String hashType, Object salt) {
        if (hashType == null) {
            hashType = HashCrypt.SHA;
        }
        if (hashType.equals(HashCrypt.MD5)) {
            return HashCrypt.md5PasswordEncoder.encodePassword(str, salt);
        }
        return HashCrypt.shaPasswordEncoder.encodePassword(str, salt);
    }
}

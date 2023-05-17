package crypto.system;

public class CaesarCipher {
    private int key;

    public CaesarCipher(int key) {
        this.key = key;
    }

    public String encryptEnglish(String message) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (Character.isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    char encryptedChar = (char) ((ch + key - 'A') % 26 + 'A');
                    encryptedMessage.append(encryptedChar);
                } else {
                    char encryptedChar = (char) ((ch + key - 'a') % 26 + 'a');
                    encryptedMessage.append(encryptedChar);
                }
            } else {
                encryptedMessage.append(ch);
            }
        }
        return encryptedMessage.toString();
    }

    public String decryptEnglish(String encryptedMessage) {
        StringBuilder decryptedMessage = new StringBuilder();
        for (int i = 0; i < encryptedMessage.length(); i++) {
            char ch = encryptedMessage.charAt(i);
            if (Character.isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    char decryptedChar = (char) ((ch - key - 'A' + 26) % 26 + 'A');
                    decryptedMessage.append(decryptedChar);
                } else {
                    char decryptedChar = (char) ((ch - key - 'a' + 26) % 26 + 'a');
                    decryptedMessage.append(decryptedChar);
                }
            } else {
                decryptedMessage.append(ch);
            }
        }
        return decryptedMessage.toString();
    }

    public String encryptUkrainian(String message) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (Character.isLetter(ch)) {
                char encryptedChar = (char) ((ch + key - 'А') % 32 + 'А');
                encryptedMessage.append(encryptedChar);
            } else {
                encryptedMessage.append(ch);
            }
        }
        return encryptedMessage.toString();
    }

    public String decryptUkrainian(String encryptedMessage) {
        StringBuilder decryptedMessage = new StringBuilder();
        for (int i = 0; i < encryptedMessage.length(); i++) {
            char ch = encryptedMessage.charAt(i);
            if (Character.isLetter(ch)) {
                char decryptedChar = (char) ((ch - key - 'А' + 32) % 32 + 'А');
                decryptedMessage.append(decryptedChar);
            } else {
                decryptedMessage.append(ch);
            }
        }
        return decryptedMessage.toString();
    }
}

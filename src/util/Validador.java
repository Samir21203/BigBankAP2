
package util;

public class Validador {
    
    public static boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        if (cpfLimpo.length() != 11) {
            return false;
        }
        
        return !cpfLimpo.matches("(\\d)\\1{10}");
    }
    
    public static boolean isCampoVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
    
    public static boolean isEmailValido(String email) {
        return !isCampoVazio(email);
    }
}

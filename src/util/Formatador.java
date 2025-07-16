/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 *
 * @author Victor
 */
public class Formatador {
    // Define o padrão brasileiro para formatação de moeda
    private static final Locale BRASIL = new Locale("pt", "BR");
    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(BRASIL);
    
    // Define o padrão brasileiro para formatação de datas
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(BRASIL);
    
    // Formata um valor numérico para o padrão de moeda brasileiro (R$)
    public static String formatarMoeda (double valor) {
        return FORMATO_MOEDA.format(valor);
    }
    
    // Formata um objeto LocalDate para o padrão de data brasileiro (dia/mes/ano)
    public static String formatarData (LocalDate data) {
        if (data == null) {
            return "";
        }
        return data.format(FORMATO_DATA);
    }
}

package aeroport.bf.service.util;


import java.math.BigDecimal;

import java.text.DecimalFormat;

import java.text.DecimalFormatSymbols;



public class ServiceUtils {

   public ServiceUtils() {

   }



   public static String getMontantInFormatMilliers(BigDecimal montant) {

      DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();

      symbols.setGroupingSeparator(' ');

      DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);

      return formatter.format(montant);

   }


}
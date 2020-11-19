package me.skiincraft.discord.herostats.utils;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class IntegerUtils {

	public static boolean isNumeric(String strNum) {
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		if (strNum == null) {
			return false;
		}
		return pattern.matcher(strNum).matches();
	}
	
	public static String getPorcentagem(int valororiginal, int valorsubtraido) {

		float v1 = Float.parseFloat(valororiginal + "");
		float v2 = Float.parseFloat(valorsubtraido + "");

		float resultado = (v2 * 100) / v1;

		DecimalFormat decimal = new DecimalFormat("#.0");
		return decimal.format(resultado) + "%";
	}

	public static String getPorcentagem(float valororiginal, float valorsubtraido) {

		float resultado = (valorsubtraido * 100) / valororiginal;

		DecimalFormat decimal = new DecimalFormat("#.0");
		return decimal.format(resultado) + "%";
	}

}

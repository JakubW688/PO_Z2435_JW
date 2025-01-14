package com.myapp.utils;

public class PriceCalculator {
    private static final double VAT_RATE = 0.23;
    public static double calculateGrossPrice(double nettoPrice) {
        return nettoPrice * (1 + VAT_RATE);
    }
}

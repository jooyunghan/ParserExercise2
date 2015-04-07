package com.jooyunghan.parser;

/**
 * Created by jooyung.han on 4/7/15.
 */
public class QuotedPrintable {
    public static class Decoder {
        public static Decoder INSTANCE = new Decoder();

        public byte[] decode(String encoded) {
            return encoded.getBytes();
        }

    }
    public static Decoder getDecoder() {
        return Decoder.INSTANCE;
    }
}

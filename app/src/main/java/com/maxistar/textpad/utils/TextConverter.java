package com.maxistar.textpad.utils;

/**
 * Contiene informacion relacionada con la modificacion de texto
 */
public class TextConverter {
    /**
     * String referida al salto de linea con el caracter \r\n
     */
    private static final String RN = "\r\n";
    /**
     * String referida al salto de linea \n
     */
    private static final String N = "\n";
    /**
     * String referida al salto de linea \r
     */
    private static final String R = "\r";
    /**
     * String referida al sistema operativo windows
     */
    public static final String WINDOWS = "windows";
    /**
     * String referida al sistema operativo unix
     */
    public static final String UNIX = "unix";
    /**
     * String referida al sistema operativo macos
     */
    public static final String MACOS = "macos";
    /**
     * String relacionada con la instancia del textConverter
     */
    static private TextConverter instance = null;
    /**
     * Devuleve la instancia del TextConverter
     * @return TextConverter
     */
    public static TextConverter getInstance() {
        if (instance == null) {
            instance = new TextConverter();
        }
        return instance;
    }
    /**
     * Devuleve string y aplica los valores
     * @param value
     * @param to
     * @return String
     */
    public String applyEndings(String value, String to) {

        if (WINDOWS.equals(to)) {
            value = value.replace(RN, N);
            value = value.replace(R, N);
            value = value.replace(N, RN); //simply replace unix endings to win endings
            return value;
        }

        if (UNIX.equals(to)) { //just in case it was previously read as other encoding
            value = value.replace(RN, N);
            value = value.replace(R, N);
            return value;
        }

        if (MACOS.equals(to)) {
            value = value.replace(RN, N);
            value = value.replace(R, N);
            value = value.replace(N, R); //simply replace unix endings to mac endings
            return value;
        }

        return value; //leave as is

    }
}

package com.maxistar.textpad;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;
/**
 * Contiene la funcionalidad necesaria para que funcionen todas las opciones del editor. Se usa en EditorActivity
 */
public class SettingsService {
    /**
     * SETTING_FONT
     */
        public static final String SETTING_FONT = "font";
    /**
     * SETTING_LAST_FILENAME
     */
        public static final String SETTING_LAST_FILENAME = "last_filename";
    /**
     * SETTING_AUTO_SAVE_CURRENT_FILE
     */
        public static final String SETTING_AUTO_SAVE_CURRENT_FILE = "auto_save_current_file";
    /**
     * SETTING_OPEN_LAST_FILE
     */
        public static final String SETTING_OPEN_LAST_FILE = "open_last_file";
    /**
     * SETTING_DELIMITERS
     */
        public static final String SETTING_DELIMITERS = "delimeters";
    /**
     * SETTING_FILE_ENCODING
     */
        public static final String SETTING_FILE_ENCODING = "encoding";
    /**
     * SETTING_FONT_SIZE
     */
        public static final String SETTING_FONT_SIZE = "fontsize";
    /**
     * SETTING_BG_COLOR
     */
        public static final String SETTING_BG_COLOR = "bgcolor";
    /**
     * SETTING_FONT_COLOR
     */
        public static final String SETTING_FONT_COLOR = "fontcolor";
    /**
     * SETTING_LANGUAGE
     */
        public static final String SETTING_LANGUAGE = "language";
    /**
     * SETTING_LEGASY_FILE_PICKER
     */
        public static final String SETTING_LEGASY_FILE_PICKER = "use_legacy_file_picker";

    /**
     * SETTING_MEDIUM
     */
        public static final String SETTING_MEDIUM = "Medium";
    /**
     * SETTING_EXTRA_SMALL
     */
        public static final String SETTING_EXTRA_SMALL = "Extra Small";
    /**
     * SETTING_SMALL
     */
        public static final String SETTING_SMALL = "Small";
    /**
     * SETTING_LARGE
     */
        public static final String SETTING_LARGE = "Large";
    /**
     * SETTING_HUGE
     */
        public static final String SETTING_HUGE = "Huge";
    /**
     * open_last_file
     */
        private boolean open_last_file = true;
    /**
     * legacy_file_picker
     */
        private boolean legacy_file_picker = false;

    /**
     * file_encoding
     */
        private String file_encoding = "";
    /**
     * last_filename
     */
        private String last_filename = "";
    /**
     * delimiters
     */
        private String delimiters;
    /**
     * font
     */
        private String font;
    /**
     * font_size
     */
        private String font_size;
    /**
     * language
     */
        private String language;
    /**
     * bgcolor
     */
        private int bgcolor;
    /**
     * fontcolor
     */
        private int fontcolor;
    /**
     * languageWasChanged
     */
        private static boolean languageWasChanged = false;

    /**
     * Constructor de la clase
     * @param context
     */
        SettingsService(Context context) {
            loadSettings(context);
        }
    /**
     * Carga las opciones
     * @param context
     */
        private void loadSettings(Context context) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            open_last_file = sharedPref.getBoolean(SETTING_OPEN_LAST_FILE, false);
            legacy_file_picker = sharedPref.getBoolean(SETTING_LEGASY_FILE_PICKER, false);
            last_filename = sharedPref.getString(SETTING_LAST_FILENAME, TPStrings.EMPTY);
            file_encoding = sharedPref.getString(SETTING_FILE_ENCODING, TPStrings.UTF_8);
            delimiters = sharedPref.getString(SETTING_DELIMITERS, TPStrings.EMPTY);
            font = sharedPref.getString(SETTING_FONT, TPStrings.FONT_SANS_SERIF);
            font_size = sharedPref.getString(SETTING_FONT_SIZE, SETTING_MEDIUM);
            bgcolor = sharedPref.getInt(SETTING_BG_COLOR, 0xFFCCCCCC);
            fontcolor = sharedPref.getInt(SETTING_FONT_COLOR, 0xFF000000);
            language = sharedPref.getString(SETTING_LANGUAGE, TPStrings.EMPTY);
        }
    /**
     * Recarga las opciones
     * @param context
     */
        public void reloadSettings(Context context) {
            loadSettings(context);
        }
    /**
     * Da valor en las opciones
     * @param name
     * @param value
     * @param context
     */
        private void setSettingValue(String name, String value, Context context) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(name, value);
            editor.apply();
        }
    /**
     * Da valor en las opciones
     * @param name
     * @param value
     * @param context
     */
        private void setSettingValue(String name, int value, Context context) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(name, value);
            editor.apply();
        }
    /**
     * Da valor en las opciones
     * @param name
     * @param value
     * @param context
     */
        private void setSettingValue(String name, boolean value, Context context) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(name, value);
            editor.apply();
        }
    /**
     * ¿Ha sido abierto el ultimo fichero?
     * Devuelve true en caso afirmativo y false en caso contrario
     * @return boolean
     */
        public boolean isOpenLastFile() {
            return open_last_file;
        }
    /**
     * ¿Es el fichero elegido un fichero de legado?
     * Devuelve true en caso afirmativo y false en caso contrario
     * @return boolean
     */
        public boolean isLegacyFilePicker() {
            return legacy_file_picker;
        }
    /**
     * Devuelve la codificacion del archivo
     * @return String
     */
        public String getFileEncoding() {
            return file_encoding;
        }
    /**
     * Devuelve los delimitadores del archivo
     * @return String
     */
        public String getDelimiters() {
            return delimiters;
        }
    /**
     * Devuelve el tamaño de fuente del archivo
     * @return String
     */
        public String getFontSize() {
            return font_size;
        }
    /**
     * Devuelve el color de fondo del archivo
     * @return String
     */
        public int getBgColor() {
            return bgcolor;
        }
    /**
     * Devuelve la fuente del color
     * @return String
     */
        public int getFontColor() {
            return fontcolor;
        }
    /**
     * Devuelve el nombre del ultimo fichero abierto
     * @return String
     */
        public String getLastFilename() {
            return last_filename;
        }
    /**
     * Devuelve la fuente
     * @return String
     */
        public String getFont() {
            return font;
        }
    /**
     * Devuelve el lenguaje
     * @return String
     */
        public String getLanguage() {
            return language;
        }
    /**
     * Asigna true si el archivo es un archivo de legado y falso en caso contrario
     * @param value
     */
        public void setLegacyFilePicker(boolean value) {
            legacy_file_picker = value;
        }
    /**
     * Asigna true si el archivo es un archivo de legado y falso en caso contrario
     * @param value
     * @param context
     */
        public void setLegacyFilePicker(boolean value, Context context) {
            this.setSettingValue(SETTING_LEGASY_FILE_PICKER, legacy_file_picker, context);
            legacy_file_picker = value;
        }
    /**
     * Asigna el tamaño de fuente
     * @param font_size
     * @param context
     */
        //setters
        public void setFontSize(String font_size, Context context) {
            this.setSettingValue(SETTING_FONT_SIZE, font_size, context);
            this.font_size = font_size;
        }
    /**
     * Asigna el color de fondo
     * @param bgcolor
     * @param context
     */
        public void setBgColor(int bgcolor, Context context) {
            this.setSettingValue(SETTING_BG_COLOR, bgcolor, context);
            this.bgcolor = bgcolor;
        }
    /**
     * Asigna el color de la fuente
     * @param fontcolor
     * @param context
     */
        public void setFontColor(int fontcolor, Context context) {
            this.setSettingValue(SETTING_FONT_COLOR, fontcolor, context);
            this.fontcolor = fontcolor;
        }
    /**
     * Asigna el nombre del ultimo archivo abierto
     * @param value
     * @param context
     */
        public void setLastFilename(String value, Context context) {
            this.setSettingValue(SETTING_LAST_FILENAME, value, context);
            last_filename = value;
        }
    /**
     * Asigna la fuente
     * @param value
     * @param context
     */
        public void setFont(String value, Context context) {
            this.setSettingValue(SETTING_FONT, value, context);
            font = value;
        }
    /**
     * Asigna el leguaje
     */
        static public void setLanguageChangedFlag() {
            languageWasChanged = true;
        }
    /**
     * ¿Ha sido cambiado el idioma del archivo? Devuelve true en caso afirmativo y false en caso contrario
     * @return boolean
     */
        static public boolean isLanguageWasChanged() {
            boolean value = languageWasChanged;
            languageWasChanged = false;
            return value;
        }
    /**
     * Aplica el lenguaje local
     * @param context
     */
        public void applyLocale(Context context){
            String lang = getLanguage();
            if ("".equals(lang)) {
                return; //use system default
            }
            Locale locale2 = new Locale(lang);
            Locale.setDefault(locale2);
            Configuration config2 = new Configuration();
            config2.locale = locale2;
            context.getResources().updateConfiguration(config2, null);
        }
}

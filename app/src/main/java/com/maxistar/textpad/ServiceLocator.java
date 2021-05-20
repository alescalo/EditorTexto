package com.maxistar.textpad;

import android.content.Context;
/**
 * Esta clase se usa para detectar la localización del archivo.
 */
public class ServiceLocator {
    /**
     * Instancia
     */
    private static ServiceLocator instance = null;

    /**
     * Metodo vacio
     */
    private ServiceLocator() {}

    /**
     * Servicio de opciones
     */
    private SettingsService settingsService;

    /**
     * Crea una instancia de ServiceLocator
     */
    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }
    /**
     * Devuleve el servicio de opciones
     * @param context
     * @return SettingsService
     */
    public SettingsService getSettingsService(Context context) {
        if (settingsService == null) {
            settingsService = new SettingsService(context);
        }
        return settingsService;
    }
}

package com.maxistar.textpad.utils;

import java.util.LinkedList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.widget.TextView;
/**
 * Clase EditTextUndoRedo
 *
 *
 * @version 1.0
 */
public class EditTextUndoRedo {

    /**
     * ¿Se está realizando deshacer / rehacer? Este miembro indica si deshacer / rehacer
     * La operación se está realizando actualmente. Cambios en el texto durante
     * deshacer / rehacer no se registran porque estropearía el historial de deshacer.
     */
    private boolean mIsUndoOrRedo = false;

    /**
     * El historial de ediciones.
     */
    private EditHistory mEditHistory;

    /**
     * El listener del cambio.
     */
    private EditTextChangeListener mChangeListener;

    /**
     * El edit text.
     */
    private TextView mTextView;

    // =================================================================== //

    /**
     * Cree un nuevo TextViewUndoRedo y lo añade al TextView especificado.
     *
     * @param textView
     *            La vista de texto para la que se implementa deshacer / rehacer.
     */
    public EditTextUndoRedo(TextView textView) {
        mTextView = textView;
        mEditHistory = new EditHistory();
        mChangeListener = new EditTextChangeListener();
        mTextView.addTextChangedListener(mChangeListener);
    }

    // =================================================================== //

    /**
     * Desconecta este deshacer / rehacer de la vista de texto.
     */
    public void disconnect() {
        mTextView.removeTextChangedListener(mChangeListener);
    }

    /**
     * Establezca el tamaño máximo del historial. Si el tamaño es negativo, el tamaño del historial solo está limitado por la memoria del dispositivo.
     * @param maxHistorySize
     */
    public void setMaxHistorySize(int maxHistorySize) {
        mEditHistory.setMaxHistorySize(maxHistorySize);
    }

    /**
     * Limpia historial
     */
    public void clearHistory() {
        mEditHistory.clear();
    }

    /**
     * ¿Se puede deshacer?
     * @return boolean
     */
    public boolean getCanUndo() {
        return (mEditHistory.mmPosition > 0);
    }

    /**
     * Realizar deshacer.
     */
    public void undo() {
        EditItem edit = mEditHistory.getPrevious();
        if (edit == null) {
            return;
        }

        Editable text = mTextView.getEditableText();
        int start = edit.mmStart;
        int end = start + (edit.mmAfter != null ? edit.mmAfter.length() : 0);

        mIsUndoOrRedo = true;
        text.replace(start, end, edit.mmBefore);
        mIsUndoOrRedo = false;

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (Object o : text.getSpans(0, text.length(), UnderlineSpan.class)) {
            text.removeSpan(o);
        }

        Selection.setSelection(text, edit.mmBefore == null ? start
                : (start + edit.mmBefore.length()));
    }

    /**
     * ¿Se puede rehacer?
     * @return boolean
     */
    public boolean getCanRedo() {
        return (mEditHistory.mmPosition < mEditHistory.mmHistory.size());
    }

    /**
     * Realizar rehacer
     */
    public void redo() {
        EditItem edit = mEditHistory.getNext();
        if (edit == null) {
            return;
        }

        Editable text = mTextView.getEditableText();
        int start = edit.mmStart;
        int end = start + (edit.mmBefore != null ? edit.mmBefore.length() : 0);

        mIsUndoOrRedo = true;
        text.replace(start, end, edit.mmAfter);
        mIsUndoOrRedo = false;

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (Object o : text.getSpans(0, text.length(), UnderlineSpan.class)) {
            text.removeSpan(o);
        }

        Selection.setSelection(text, edit.mmAfter == null ? start
                : (start + edit.mmAfter.length()));
    }

    /**
     * Preferencias de la tienda.
     * @param editor
     * @param prefix
     */
    public void storePersistentState(Editor editor, String prefix) {
        // Store hash code of text in the editor so that we can check if the
        // editor contents has changed.
        editor.putString(prefix + ".hash",
                String.valueOf(mTextView.getText().toString().hashCode()));
        editor.putInt(prefix + ".maxSize", mEditHistory.mmMaxHistorySize);
        editor.putInt(prefix + ".position", mEditHistory.mmPosition);
        editor.putInt(prefix + ".size", mEditHistory.mmHistory.size());

        int i = 0;
        for (EditItem ei : mEditHistory.mmHistory) {
            String pre = prefix + "." + i;

            editor.putInt(pre + ".start", ei.mmStart);
            editor.putString(pre + ".before", ei.mmBefore.toString());
            editor.putString(pre + ".after", ei.mmAfter.toString());

            i++;
        }
    }

    /**
     * Restaura preferencias
     *
     * @param prefix
     *            El prefijo de la clave de preferencia utilizado cuando se almacenó el estado
     * @return ¿La restauración tuvo éxito? Si esto es falso, el historial de deshacer será
     * vacío.
     */
    public boolean restorePersistentState(SharedPreferences sp, String prefix)
            throws IllegalStateException {

        boolean ok = doRestorePersistentState(sp, prefix);
        if (!ok) {
            mEditHistory.clear();
        }

        return ok;
    }
    /**
     * Restaurar estado persistente
     *
     * @param sp
     * @param prefix
     *
     * @return boolean
     */
    private boolean doRestorePersistentState(SharedPreferences sp, String prefix) {

        String hash = sp.getString(prefix + ".hash", null);
        if (hash == null) {
            // No state to be restored.
            return true;
        }

        if (Integer.parseInt(hash) != mTextView.getText().toString().hashCode()) {
            return false;
        }

        mEditHistory.clear();
        mEditHistory.mmMaxHistorySize = sp.getInt(prefix + ".maxSize", -1);

        int count = sp.getInt(prefix + ".size", -1);
        if (count == -1) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            String pre = prefix + "." + i;

            int start = sp.getInt(pre + ".start", -1);
            String before = sp.getString(pre + ".before", null);
            String after = sp.getString(pre + ".after", null);

            if (start == -1 || before == null || after == null) {
                return false;
            }
            mEditHistory.add(new EditItem(start, before, after));
        }

        mEditHistory.mmPosition = sp.getInt(prefix + ".position", -1);
        return mEditHistory.mmPosition != -1;
    }

    // =================================================================== //

    /**
     * Realiza un seguimiento de todo el historial de edición de un texto.
     */
    private static final class EditHistory {

        /**
         * La posición desde la que se recuperará un EditItem cuando se llame a getNext (). Si no se ha llamado a getPrevious (), tiene el mismo valor que mmHistory.size ().
         */
        private int mmPosition = 0;

        /**
         * Tamaño máximo del historial de deshacer.
         */
        private int mmMaxHistorySize = -1;

        /**
         * La lista de ediciones en orden cronológico.
         */
        private final LinkedList<EditItem> mmHistory = new LinkedList<>();

        /**
         * Limpiar historial
         */
        private void clear() {
            mmPosition = 0;
            mmHistory.clear();
        }

        /**
         * Agrega una nueva operación de edición al historial en la posición actual. Si se ejecuta después de una llamada a getPrevious (), elimina todo el historial futuro (elementos con posiciones> = posición actual del historial).
         */
        private void add(EditItem item) {
            while (mmHistory.size() > mmPosition) {
                mmHistory.removeLast();
            }
            mmHistory.add(item);
            mmPosition++;

            if (mmMaxHistorySize >= 0) {
                trimHistory();
            }
        }

        /**
         * Establezca el tamaño máximo del historial. Si el tamaño es negativo, el tamaño del historial solo está limitado por la memoria del dispositivo.
         */
        private void setMaxHistorySize(int maxHistorySize) {
            mmMaxHistorySize = maxHistorySize;
            if (mmMaxHistorySize >= 0) {
                trimHistory();
            }
        }

        /**
         * Recorte el historial cuando supere el tamaño máximo del historial.
         */
        private void trimHistory() {
            while (mmHistory.size() > mmMaxHistorySize) {
                mmHistory.removeFirst();
                mmPosition--;
            }

            if (mmPosition < 0) {
                mmPosition = 0;
            }
        }

        /**
         * Recorre el historial hacia atrás en una posición, devuelve un artículo en esa posición.
         */
        private EditItem getPrevious() {
            if (mmPosition == 0) {
                return null;
            }
            mmPosition--;
            return mmHistory.get(mmPosition);
        }

        /**
         * Recorre el historial hacia adelante en una posición, devuelve un artículo en esa posición.
         */
        private EditItem getNext() {
            if (mmPosition >= mmHistory.size()) {
                return null;
            }

            EditItem item = mmHistory.get(mmPosition);
            mmPosition++;
            return item;
        }
    }

    /**
     * Representa los cambios realizados por una sola operación de edición.
     */
    private static final class EditItem {
        private final int mmStart;
        private final CharSequence mmBefore;
        private final CharSequence mmAfter;

        /**
         * Construye EditItem de una modificación que se aplicó al inicio de la posición y reemplazó CharSequence antes por CharSequence después.
         */
        public EditItem(int start, CharSequence before, CharSequence after) {
            mmStart = start;
            mmBefore = before;
            mmAfter = after;
        }
    }

    /**
     * Clase que escucha cambios en el texto.
     */
    private final class EditTextChangeListener implements TextWatcher {

        /**
         * El texto que será eliminado por el evento de cambio.
         */
        private CharSequence mBeforeChange;

        /**
         * El texto antes de ser cambiado
         * @param s
         * @param start
         * @param count
         * @param after
         */
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (mIsUndoOrRedo) {
                return;
            }

            mBeforeChange = s.subSequence(start, start + count);
        }
        /**
         * Cuando el texto es cambiado
         * @param s
         * @param start
         * @param before
         * @param count
         */
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (mIsUndoOrRedo) {
                return;
            }

            CharSequence mAfterChange = s.subSequence(start, start + count);
            mEditHistory.add(new EditItem(start, mBeforeChange, mAfterChange));
        }

        public void afterTextChanged(Editable s) {
        }
    }
}
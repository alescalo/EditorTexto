package com.maxistar.textpad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.AttributeSet;

/**
 * Esta clase no se usa actualmente en esta versión de la aplicación, está pensada para mostrar al usuario información sobre los autores de la app
 */
public class AboutBox extends DialogPreference
{
	/**
	 * Este es el constructor llamado por el inflater.
	 */
	public AboutBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	/**
	 * Se llama cuando se crea el cuadro de dialogo
	 * @param builder
	 */
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
	    // Data has changed, notify so UI can be refreshed!
		//builder.setTitle(R.string.About);
		builder.setPositiveButton(R.string.Continue, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
			}
		});
		
		final SpannableString s = 
            new SpannableString(l(R.string.about_message));
		Linkify.addLinks(s, Linkify.WEB_URLS);
		
		builder.setMessage(s);
		builder.setNegativeButton(null, null);
    }

	String l(int id) {
		return getContext().getResources().getString(id);
	}
	
}

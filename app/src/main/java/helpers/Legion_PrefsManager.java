package helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.util.Map;
import utils.Legion_Constants;

public class Legion_PrefsManager implements Legion_Constants{

	private Context context;
	private SharedPreferences prefsManager;

	public Legion_PrefsManager(Context applicationContext) {
		context = applicationContext;
		prefsManager = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void save(String key, String value) {
		Editor editor = prefsManager.edit();
		editor.putString(key, value);
		editor.apply();
	}
	public void saveBoolean(String key, Boolean value) {
		Editor editor = prefsManager.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public void saveInt(String key, int value) {
		Editor editor = prefsManager.edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public String get(String... key){
		if(key.length == 1) {
			return prefsManager.getString(key[0], null);
		}else{
			return prefsManager.getString(key[0], key[1]);
		}
	}
	public Boolean getBoolean(String... key){
			return prefsManager.getBoolean(key[0], false);
	}
	public int getInt(String... key){
		if(key.length == 1) {
			return prefsManager.getInt(key[0],0);
		}else{
			return prefsManager.getInt(key[0],0);
		}
	}

	public void clearAllPrefs() {
		prefsManager.edit().clear().apply();
	}

	public boolean hasKey(String key) {
		return prefsManager.contains(key);
	}

	public void removeKey(String key) {
		Editor editor = prefsManager.edit();
		editor.remove(key);
		editor.apply();
	}

	public Map<String, ?> getAllPrefs(){
		return prefsManager.getAll();
	}
}

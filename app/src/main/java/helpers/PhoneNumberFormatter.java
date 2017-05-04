package helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumberFormatter implements TextWatcher {

    private final EditText phoneEditText;

    public PhoneNumberFormatter(EditText phoneEditText) {
        this.phoneEditText = phoneEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public synchronized void afterTextChanged(final Editable text) {
        String number = text.toString().trim();
        number = number.replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
        int len = number.length();
        if(len > 3 && len < 7){
            number = "("+number.substring(0, 3)+") "+number.substring(3);
        }else if(len >= 7){
            number = "("+number.substring(0, 3)+") "+number.substring(3, 6)+"-"+number.substring(6);
        }
        phoneEditText.removeTextChangedListener(this);
        phoneEditText.setText(number);
        phoneEditText.setSelection(number.length());
        phoneEditText.addTextChangedListener(this);
    }
}

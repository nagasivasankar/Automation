package ui;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import cn.jeesoft.widget.pickerview.CharacterPickerView;
import cn.jeesoft.widget.pickerview.CharacterPickerWindow;
import cn.jeesoft.widget.pickerview.OnOptionChangedListener;

public class OptionsWindowHelper {

    private static List<String> optionsItems = null;

    public interface OnOptionsSelectListener {
        void onOptionsSelect(String province);
    }

    private OptionsWindowHelper() {
    }

    public static CharacterPickerWindow builder(Activity activity, final OnOptionsSelectListener listener) {
        CharacterPickerWindow mOptions = new CharacterPickerWindow(activity);
        setPickerData(mOptions.getPickerView());
        mOptions.setSelectOptions(0, 0, 0);
        mOptions.setOnoptionsSelectListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int option1, int option2, int option3) {
                if (listener != null) {
                    String province = optionsItems.get(option1);
                    listener.onOptionsSelect(province);
                }
            }
        });
        return mOptions;
    }


    public static void setPickerData(CharacterPickerView view) {
        if (optionsItems == null) {
            optionsItems = new ArrayList<>();

            /*final Map<String, Map<String, List<String>>> allCitys = ArrayDataDemo.getAll();
            for (Map.Entry<String, Map<String, List<String>>> entry1 : allCitys.entrySet()) {
                String key1 = entry1.getKey();
                Map<String, List<String>> value1 = entry1.getValue();

                optionsItems.add(key1);
            }
            Collections.sort(optionsItems);*/
            optionsItems.add("4:00 am");
            //optionsItems.add("4:30 am");
            //optionsItems.add("5:00 am");
            //optionsItems.add("5:30 am");
            optionsItems.add("6:00 am");
           // optionsItems.add("6:30 am");
           // optionsItems.add("7:00 am");
            //optionsItems.add("7:30 am");
            optionsItems.add("8:00 am");
            //optionsItems.add("8:30 am");
            //optionsItems.add("9:00 am");
            //optionsItems.add("9:30 am");
            optionsItems.add("10:00 am");
            //optionsItems.add("10:30 am");
            //optionsItems.add("11:00 am");
            //optionsItems.add("11:30 am");
            optionsItems.add("12:00 pm");
            //optionsItems.add("12:30 pm");
            ///optionsItems.add("1:00 pm");
            //optionsItems.add("1:30 pm");
            optionsItems.add("2:00 pm");
            ///optionsItems.add("2:30 pm");
            ///optionsItems.add("3:00 pm");
            //optionsItems.add("3:30 pm");
            optionsItems.add("4:00 pm");
            ///optionsItems.add("4:30 pm");
            ///optionsItems.add("5:00 pm");
            //optionsItems.add("5:30 pm");
            optionsItems.add("6:00 pm");
            //optionsItems.add("6:30 pm");
            //optionsItems.add("7:00 pm");
            //optionsItems.add("7:30 pm");
            optionsItems.add("8:00 pm");
            ///optionsItems.add("8:30 pm");
            //optionsItems.add("9:00 pm");
            //optionsItems.add("9:30 pm");
            optionsItems.add("10:00 pm");
            //optionsItems.add("10:30 pm");
            //optionsItems.add("11:00 pm");
            //optionsItems.add("11:30 pm");
            optionsItems.add("12:00 am");

        }
        view.setPicker(optionsItems);
    }

}

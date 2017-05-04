package tabs;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import co.legion.client.R;
import co.legion.client.activities.HomeActivity;

/**
 * Created by Administrator on 11/22/2016.
 */
public class BaseContainerFragment extends Fragment {
    public void replaceFragment(Fragment fragment, boolean addToBackStack, String tag){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack){
            transaction.addToBackStack(null);
        }
        if (tag != null){
            transaction.replace(R.id.containerFragment, fragment, tag);
        }else {
            transaction.replace(R.id.containerFragment, fragment);
        }
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment(){
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() >0){
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        ScheduleTabFragment scheduleTabFragment = (ScheduleTabFragment)getChildFragmentManager().findFragmentByTag(HomeActivity.TAB_2_SPEC);
        if(scheduleTabFragment != null) {
            scheduleTabFragment.onActivityResult(arg0, arg1, arg2);
        }

        ShiftOffersTabFragment shiftOffersTabFragment = (ShiftOffersTabFragment)getChildFragmentManager().findFragmentByTag(HomeActivity.TAB_3_SPEC);
        if(shiftOffersTabFragment != null) {
            shiftOffersTabFragment.onActivityResult(arg0, arg1, arg2);
        }
    }
}

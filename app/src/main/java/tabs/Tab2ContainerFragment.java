package tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.legion.client.R;
import co.legion.client.activities.HomeActivity;

/**
 * Created by Administrator on 11/22/2016.
 */
public class Tab2ContainerFragment extends BaseContainerFragment {
    boolean mIsViewInitiated;
    public static ScheduleTabFragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.container_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsViewInitiated) {
            mIsViewInitiated = false;
            initView();
        }
    }

    public Tab2ContainerFragment() {

    }

    private void initView() {
        replaceFragment(fragment = new ScheduleTabFragment(), false, HomeActivity.TAB_2_SPEC);
    }

    public ScheduleTabFragment getHostFragment() {
        return fragment;
    }
}

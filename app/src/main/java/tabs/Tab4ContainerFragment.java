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
public class Tab4ContainerFragment extends BaseContainerFragment {
    boolean mIsViewInitiated;
    public static InboxTabFragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.container_fragment, container, false);
    }
    public Tab4ContainerFragment(){

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsViewInitiated){
            mIsViewInitiated = false;
            initView();
        }
    }

    private void initView() {
        replaceFragment(fragment = new InboxTabFragment(), false, HomeActivity.TAB_4_SPEC);
    }

    public  InboxTabFragment getHostFragment(){
        return fragment;
    }
}

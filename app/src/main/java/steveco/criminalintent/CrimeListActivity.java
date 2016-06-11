package steveco.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by sretu on 3/24/2016.
 *
 *
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}

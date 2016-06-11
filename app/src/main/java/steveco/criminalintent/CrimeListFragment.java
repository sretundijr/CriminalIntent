package steveco.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by sretu on 3/24/2016.
 * when adapter gets called the methods in the crime adapter class automatically happen?
 *
 */
public class CrimeListFragment extends Fragment {

    private int mLastAdapterPositionClick = -1;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public void onCreate(Bundle savedOnInstanceState){
        super.onCreate(savedOnInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstance){
        //the following statements prepare the recycler for the call to update ui
        //creates the view object and inflates the id
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        //uses the newly created view to set recycler views id
        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        //recycler requires a layout manager, set it up here
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(onSavedInstance != null){
            mSubtitleVisible = onSavedInstance.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        //call update ui in on create view method
        updateUI();
        //return the view
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    //update ui method sets up the crime lab (stored array of crimes)
    private void updateUI(){
        //this is how a crime lab instance is created
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        //sets up a list of crimes and uses the crime lab get crimes method
        //to populate the list
        List<Crime> crimes = crimeLab.getCrimes();

        //used to update the ui after a new fragment has been loaded in the stack
        //once new frag is popped it will update the crime list when on resume is called if changes
        //were made
        if(mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            if (mLastAdapterPositionClick < 0) {
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter.setCrimes(crimes);
                //old code made the change to prevent crash after deleting a crime
                //mAdapter.notifyItemChanged(mLastAdapterPostionClick);
                mAdapter.notifyDataSetChanged();
                mLastAdapterPositionClick = -1;
            }
        }

        updateSubtitle();
        //old code
        //instantiate the adapter object with the crime adapter class and pass the list
        //of crimes
//        mAdapter = new CrimeAdapter(crimes);
//        //recycler needs an adapter use its method and pass the adapter
//        mCrimeRecyclerView.setAdapter(mAdapter);
    }
    //the recycler requires a view holder object, inner class implementation
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        //crime holder constructor needs a view item
        public CrimeHolder(View itemView){
            //pass item to the parent constructor
            super(itemView);
            //set a listener that allows the user to click on the line item
            //this represents the crime holder
            itemView.setOnClickListener(this);
            //set up the views by id
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        //the bind crime method binds to the recycler and sets the text as follows
        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(DateFormat.format("EEEE, dd MMMM yyyy", mCrime.getDate()));
            //mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        //on click here is temp and is used to show that when the user clicks a line
        //it works and it outputs the toast
        @Override
        public void onClick(View v){

            //this code is used for the challenge on pg 203 it stores the index of the last adapter
            //the user clicked on.  the position is then used to change the crime list fragment in
            //update ui
            mLastAdapterPositionClick = getAdapterPosition();

            //uses get activity to pass its hosting activity as the Context arg that intent requires
            //getActivity returns the activity this fragment is currently associated with, changes
            //were made after the previous comment was entered, new comment below

            //calls the crime activity class to get the intent by crime id
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    //crime adapter used to communicate between crime holder and recycler
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        //get a list of crimes
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        //the return statement here creates the crime holder object and passes this view
        //this view holds the id for xml
        //on create method of type crime holder
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        //uses the position with in the array and binds the crime to the holder
        public void onBindViewHolder(CrimeHolder holder, int position){
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        //return the count
        @Override
        public int getItemCount(){
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
    }

}

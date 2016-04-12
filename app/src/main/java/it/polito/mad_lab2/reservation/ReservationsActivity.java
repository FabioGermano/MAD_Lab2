package it.polito.mad_lab2.reservation;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import it.polito.mad_lab2.BaseActivity;
import it.polito.mad_lab2.GestioneDB;
import it.polito.mad_lab2.R;
import it.polito.mad_lab2.data.reservation.Reservation;
import it.polito.mad_lab2.data.reservation.ReservationEntity;
import it.polito.mad_lab2.data.reservation.ReservationTypeConverter;

public class ReservationsActivity extends BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ReservationEntity res_entity;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetCalendarButtonVisibility(true);
        hideShadow(true);
        setTitleTextView(getResources().getString(R.string.title_activity_reservations));
        setContentView(R.layout.activity_reservations);

        getReservations();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), res_entity);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    void getReservations(){
        GestioneDB db = new GestioneDB();
        this.res_entity = db.getAllReservations(getApplicationContext());
    }
    @Override
    protected void OnSaveButtonPressed() {

    }

    @Override
    protected void OnAlertButtonPressed() {

    }

    @Override
    protected void OnCalendarButtonPressed() {

    }

    @Override
    protected void OnBackButtonPressed() {

    }


    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ReservationEntity res_entity;

        public SectionsPagerAdapter(FragmentManager fm, ReservationEntity res_entity) {
            super(fm);
            this.res_entity = res_entity;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return ReservationFragment.newInstance(position + 1, res_entity.getReservationsByDateAndType("2016-04-12", ReservationTypeConverter.fromTabPosition(position)));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Pending";
                case 1:
                    return "Accepted";
                case 2:
                    return "Deleted";
                case 3:
                    return "Rejected";
            }
            return null;
        }
    }
}

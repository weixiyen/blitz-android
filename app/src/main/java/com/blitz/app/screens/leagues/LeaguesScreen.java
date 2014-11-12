package com.blitz.app.screens.leagues;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.dialogs.input.DialogInput;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.dropdown.BlitzDropdown;
import com.blitz.app.utilities.scrubber.BlitzScrubber;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelLeagues;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 10/24/14. Copyright 2014 Blitz Studios
 */
public class LeaguesScreen extends BaseFragment implements ViewModelLeagues.Callbacks,
        LeaguesScreenAdapterCreate.Callbacks, LeaguesScreenAdapterView.Callbacks, BlitzScrubber.Callbacks {

    // region Member Variables
    // ============================================================================================================

    @InjectView(R.id.blitz_dropdown_container) View mBlitzDropdownContainer;
    @InjectView(R.id.blitz_dropdown_header) TextView mBlitzDropdownHeader;
    @InjectView(R.id.blitz_dropdown_list) ListView mBlitzDropdownList;

    @InjectView(R.id.leagues_screen_list) ListView mLeaguesScreenList;

    @InjectView(R.id.blitz_scrubber) BlitzScrubber mBlitzScrubber;
    @InjectView(R.id.blitz_scrubber_selected) TextView mBlitzScrubberSelected;

    // Associated view model.
    private ViewModelLeagues mViewModel;

    // Tracks selected league.
    private int mSelectedLeague;

    // User leagues, directly maps to scrubber indices.
    private List<String> mUserLeagueIds;

    // Initialize the adapters this screen will use.
    private LeaguesScreenAdapterCreate mAdapterCreate = new LeaguesScreenAdapterCreate(this);
    private LeaguesScreenAdapterView mAdapterView = new LeaguesScreenAdapterView(this);

    // Input dialog.
    private DialogInput mDialogInput;

    // Dropdown widget.
    private BlitzDropdown mBlitzDropdown;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * A simpler version of on create view.  Can be used
     * as a convenience method which abstracts away
     * some of the details of a fragment and makes them
     * more like activities.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        // Provide the view pager and callbacks.
        mBlitzScrubber.setViewPager(((MainScreen) getActivity()).getViewPager());
        mBlitzScrubber.setCallbacks(this);
    }

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelLeagues(getBaseActivity(), this);
        }

        return mViewModel;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Set the selected league and update associated UI.
     *
     * @param selectedLeague Selected league.
     */
    private void setSelectedLeague(int selectedLeague) {

        // Update selected league index.
        mSelectedLeague = selectedLeague;

        // Ensure the correct item is selected.
        mBlitzScrubber.setScrubberItemSelected(mSelectedLeague);

        // Trigger a view model update for associated id.
        mViewModel.setSelectedLeagueId(mUserLeagueIds.get(mSelectedLeague));

        if (mUserLeagueIds.get(mSelectedLeague) == null) {

            // Set the initial adapter for create/join.
            mLeaguesScreenList.setAdapter(mAdapterCreate);
        } else {

            // Set the initial adapter for user league.
            mLeaguesScreenList.setAdapter(mAdapterView);
        }

        // Update the selected league.
        mBlitzDropdown.setSelected(selectedLeague);
    }

    // endregion

    // region View Model Callbacks
    // ============================================================================================================

    /**
     * Hide dialog when league created.
     */
    @Override
    public void onLeagueCreated() {

        if (mDialogInput != null) {
            mDialogInput.dismiss();
        }
    }

    /**
     * Hide dialog when league joined.
     */
    @Override
    public void onLeagueJoined() {

        if (mDialogInput != null) {
            mDialogInput.dismiss();
        }
    }

    /**
     * Received all available user leagues.
     *
     * @param leagueIds League ids. The last is null (represents
     *                  the create/join screen).
     * @param leagueNames League names.
     */
    @Override
    public void onUserLeagues(List<String> leagueIds, List<String> leagueNames) {

        mUserLeagueIds = leagueIds;

        // Set the scrubber text view, names, and size.
        mBlitzScrubber.setScrubberTextView(mBlitzScrubberSelected, leagueNames);
        mBlitzScrubber.setSize(mUserLeagueIds.size());

        // Configure the dropdown.
        mBlitzDropdown = new BlitzDropdown(getActivity(), leagueIds, leagueNames);
        mBlitzDropdown.setListView(mBlitzDropdownList);
        mBlitzDropdown.setHeaderView(mBlitzDropdownHeader);
        mBlitzDropdown.setContainerView(mBlitzDropdownContainer);

        // Initialize selected league.
        setSelectedLeague(mSelectedLeague);
    }

    /**
     * On recruiting leagues data received.
     *
     * @param leagueIds League ids.
     * @param leagueNames League names.
     * @param leagueRatings League ratings.
     * @param leagueMemberCounts League member counts.
     */
    @Override
    public void onRecruitingLeagues(List<String> leagueIds,
                                    List<String> leagueNames,
                                    List<Integer> leagueRatings,
                                    List<Integer> leagueMemberCounts) {

        if (mLeaguesScreenList != null) {

            // Set recruiting leagues.
            mAdapterCreate.setAssociatedListView(mLeaguesScreenList);
            mAdapterCreate.setRecruitingLeagues
                    (leagueIds, leagueNames, leagueRatings, leagueMemberCounts);
        }
    }

    /**
     * User league details received.
     *
     * @param leagueRank Rank.
     * @param leagueRating Rating.
     * @param leagueMembers Members.
     * @param memberUserIds User ids.
     * @param memberUserNames User names.
     * @param memberWins Wins.
     * @param memberLosses Losses.
     * @param memberRating Rating.
     */
    @Override
    public void onUserLeague(String leagueId, int leagueRank, int leagueRating, int leagueMembers,
                             boolean isOfficer, boolean isRecruiting,
                             List<String>  memberUserIds,
                             List<String>  memberUserNames,
                             List<Integer> memberWins,
                             List<Integer> memberLosses,
                             List<Integer> memberRating) {

        if (mLeaguesScreenList != null) {

            // Set league info.
            mAdapterView.setAssociatedListView(mLeaguesScreenList);
            mAdapterView.setLeagueInfo(leagueId, leagueRank, leagueRating, leagueMembers, isOfficer, isRecruiting,
                    memberUserIds, memberUserNames, memberWins, memberLosses, memberRating);
        }
    }

    // endregion

    // region Misc Callbacks
    // ============================================================================================================

    /**
     * Attempt to join the specified league.
     *
     * @param leagueId League to join.
     */
    @Override
    public void onJoinLeagueClicked(String leagueId) {

        // Try to join the associated league.
        mViewModel.joinLeagueWithId(leagueId);
    }

    /**
     * Create a dialog to allow the user to
     * input a league name they want.
     */
    @Override
    public void onJoinLeagueManualClicked() {

        if (mDialogInput != null) {
            mDialogInput.dismiss();
        }

        // Create and configure the input dialog.
        mDialogInput = DialogInput.create(new DialogInput.Callbacks() {

              @Override
              public void onDialogButtonLeftPressed(DialogInput dialogInput) {

                  // Hide the dialog.
                  dialogInput.dismiss();
              }

              @Override
              public void onDialogButtonRightPressed(DialogInput dialogInput) {

                  // Try to join the league.
                  mViewModel.joinLeagueWithName(dialogInput.getInputtedText());
              }
          },
                R.string.join_a_league,
                R.string.join_a_league_description,
                R.string.enter_league_name,
                R.string.cancel,
                R.string.join);

        // Show the input dialog.
        mDialogInput.show(getChildFragmentManager());
    }

    /**
     * Create a dialog to allow user
     * to input the league name they want.
     */
    @Override
    public void onCreateLeagueClicked() {

        if (mDialogInput != null) {
            mDialogInput.dismiss();
        }

        // Create and configure the input dialog.
        mDialogInput = DialogInput.create(new DialogInput.Callbacks() {

            @Override
            public void onDialogButtonLeftPressed(DialogInput dialogInput) {

                // Hide the dialog.
                dialogInput.dismiss();
            }

            @Override
            public void onDialogButtonRightPressed(DialogInput dialogInput) {

                // Try to create the league.
                mViewModel.createLeagueWithName(dialogInput.getInputtedText());
            }
        },
                R.string.create_league,
                R.string.create_league_description,
                R.string.enter_league_name,
                R.string.cancel,
                R.string.create);

        // Show the input dialog.
        mDialogInput.show(getChildFragmentManager());
    }

    /**
     * Update selected league UI when
     * the scrubber position changes.
     *
     * @param position Current position.
     */
    @Override
    public void onScrubberItemSelected(int position) {

        // Update selected league.
        setSelectedLeague(position);
    }

    /**
     * Recruiting status changes for a league.
     *
     * @param leagueId Associated league.
     * @param recruiting Recruiting status.
     */
    @Override
    public void onRecruiting(String leagueId, boolean recruiting) {

        // Toggle recruiting status for the selected league.
        mViewModel.toggleRecruiting(leagueId, recruiting);
    }

    // endregion
}
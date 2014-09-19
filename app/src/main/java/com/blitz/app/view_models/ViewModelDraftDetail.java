package com.blitz.app.view_models;

import android.app.Activity;
import android.util.Pair;

import com.blitz.app.object_models.ObjectModelStats;
import com.blitz.app.simple_models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for draft detail page.
 *
 * Created by Nate on 9/10/14.
 */
public class ViewModelDraftDetail extends ViewModel {

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelDraftDetail(Activity activity, ViewModelDraftDetailCallbacks callbacks) {
        super(activity, callbacks);
    }

    @Override
    public void initialize() {
        // TODO remove this test code
        Player p1 = new Player("Johnny Football", "SF", "QB", 2.234f);
        Player p2 = new Player("Ivan Drago", "MOS", "QB", 4.555f);

        List<Pair<Player, Player>> testPlayers = new ArrayList<Pair<Player, Player>>();
        testPlayers.add(Pair.create(p1, p2));
        testPlayers.add(Pair.create(p1, p1));
        testPlayers.add(Pair.create(p2, p1));

        ViewModelDraftDetailCallbacks callbacks =
        getCallbacks(ViewModelDraftDetailCallbacks.class);

        callbacks.onMatchup("Galfgarion", 1.23f, "mericsson", 4.45f);

        ObjectModelStats.fetchStats(callbacks);


        //callbacks.onPlayers(testPlayers);

    }

    public interface ViewModelDraftDetailCallbacks extends ViewModelCallbacks {

        void onPlayers(List<Pair<Player, Player>> players);
        void onMatchup(String player1Name, float player1score, String player2Name, float player2Score);
    }
}

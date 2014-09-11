package com.blitz.app.view_models;

import android.util.Pair;

import com.blitz.app.simple_models.Player;

import java.util.List;

/**
 * View model for draft detail page.
 *
 * Created by Nate on 9/10/14.
 */
public class ViewModelDraftDetail {

    public interface ViewModelDraftDetailCallbacks {

        void onPlayers(List<Pair<Player, Player>> players);
    }
}

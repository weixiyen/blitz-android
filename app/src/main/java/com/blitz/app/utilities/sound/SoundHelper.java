package com.blitz.app.utilities.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.blitz.app.utilities.app.AppConfig;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class SoundHelper {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Instance object.
    private static SoundHelper mInstance;

    // Application level context.
    private Context mContext;

    // Media players - boolean flag is to track paused state.
    private ArrayList<MediaPlayerHelper> mMediaPlayers;

    // Is music disabled.
    private boolean mMusicDisabled;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Fetch singleton.
     *
     * @return Singleton object.
     */
    public static SoundHelper instance() {

        if (mInstance == null) {
            synchronized (SoundHelper.class) {

                if (mInstance == null) {
                    mInstance = new SoundHelper();
                    mInstance.mMediaPlayers = new ArrayList<MediaPlayerHelper>();
                }
            }
        }

        return mInstance;
    }

    /**
     * Initialize sound helper with a context.
     *
     * @param context Context object (preferably application level).
     */
    public static void init(Context context) {
        instance().mContext = context;
    }

    /**
     * Start playing associated resource.
     *
     * @param resourceId Resource.
     */
    @SuppressWarnings("unused")
    public void startMusic(int resourceId) {

        // Loop single track.
        startMusic(resourceId, null);
    }

    /**
     * Starts playing associated resource.  The first
     * resource plays followed by the second on a loop.
     *
     * @param resourceId First.
     * @param resourceIdLoop Second (loop).
     */
    public void startMusic(int resourceId, Integer resourceIdLoop) {

        // Initialize all players.
        initializePlayers();

        // Create a player for the first resource.
        MediaPlayer mediaPlayer = createPlayer(resourceId, false);

        // Wait for it to be prepared before playing.
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            @SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
            public void onPrepared(MediaPlayer mediaPlayer) {

                if (!mMusicDisabled && AppConfig.SOUND_ENABLED) {

                    // Start playing music.
                    mediaPlayer.start();
                }
            }
        });

        if (resourceIdLoop != null) {

            // Create a new looping player from the second track.
            final MediaPlayer mediaPlayerLoop = createPlayer(resourceIdLoop, true);

            // Play it when the first track finishes.
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    mediaPlayerLoop.start();
                }
            });
        }
    }

    /**
     * Stop playing music.
     */
    @SuppressWarnings("unused")
    public void stopMusic() {
        if (!mMusicDisabled) {

            // Re-initialize.
            initializePlayers();
        }
    }

    /**
     * Pause music.
     */
    @SuppressWarnings("unused")
    public void pauseMusic() {

        // Iterate over each media player.
        for (MediaPlayerHelper mediaPlayerHelper : mMediaPlayers) {

            MediaPlayer mediaPlayer = mediaPlayerHelper.getMediaPlayer();

            // If initialized and not playing, pause it.
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();

                // Now paused.
                mediaPlayerHelper.setPaused(true);
            }
        }
    }

    /**
     * Resume music.
     */
    @SuppressWarnings("unused")
    public void resumeMusic() {

        // Iterate over each media player.
        for (MediaPlayerHelper mediaPlayerHelper : mMediaPlayers) {

            MediaPlayer mediaPlayer = mediaPlayerHelper.getMediaPlayer();

            // If initialized and not playing or disabled.
            if (mediaPlayer != null && mediaPlayerHelper.getPaused() && !mMusicDisabled) {
                mediaPlayer.start();

                // No longer paused.
                mediaPlayerHelper.setPaused(false);
            }
        }
    }

    /**
     * Is the entire music player disabled.
     *
     * @param musicDisabled Disabled flag.
     */
    @SuppressWarnings("unused")
    public void setMusicDisabled(boolean musicDisabled) {

        mMusicDisabled = musicDisabled;

        if (mMusicDisabled) {

            // Pause if disabled.
            pauseMusic();
        } else {

            // Just resume.
            resumeMusic();
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Initialize the player.  First make sure
     * that it is released.
     */
    private void initializePlayers() {
        if (mMediaPlayers != null) {

            // Iterate over each media player.
            for (MediaPlayerHelper mediaPlayerHelper : mMediaPlayers) {

                if (mediaPlayerHelper.getMediaPlayer() != null) {
                    mediaPlayerHelper.getMediaPlayer().release();
                }
            }

            // Remove all media players.
            mMediaPlayers.clear();
        }
    }

    /**
     * Create and return a new player.
     *
     * @return Initialized player.
     */
    private MediaPlayer createPlayer(int resourceId, boolean looping) {

        MediaPlayer mediaPlayer = new MediaPlayer();

        // Set the data source.
        setDataSource(mediaPlayer, resourceId);

        // Normal volume.
        mediaPlayer.setVolume(100, 100);

        // Prepare it async.
        mediaPlayer.prepareAsync();

        // Set the looping flag.
        mediaPlayer.setLooping(looping);

        // Add it to the array.
        mMediaPlayers.add(new MediaPlayerHelper(mediaPlayer));

        return mediaPlayer;
    }

    /**
     * Set the data source for a media player.
     *
     * @param mediaPlayer Media player object.
     * @param resourceId Target resource.
     */
    private void setDataSource(MediaPlayer mediaPlayer, int resourceId) {

        try {
            AssetFileDescriptor assetFileDescriptor = mContext.getResources()
                    .openRawResourceFd(resourceId);

            mediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getDeclaredLength());

            assetFileDescriptor.close();
        }
        catch (IllegalArgumentException ignored) { }
        catch (IllegalStateException    ignored) { }
        catch (IOException              ignored) { }
    }

    //==============================================================================================
    // Inner Class
    //==============================================================================================

    /**
     * Wrapper class to keep track
     * of media players.
     */
    private class MediaPlayerHelper {

        // Native android media player.
        private MediaPlayer mMediaPlayer;

        // Is this player paused.
        private boolean mPaused;

        public MediaPlayerHelper(MediaPlayer mediaPlayer) {
            mMediaPlayer = mediaPlayer;
        }

        public MediaPlayer getMediaPlayer() {
            return mMediaPlayer;
        }

        public void setPaused(boolean paused) {
            mPaused = paused;
        }

        public boolean getPaused() {
            return mPaused;
        }
    }
}
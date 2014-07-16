package com.blitz.app.utilities.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

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

    // Media player.5
    private MediaPlayer mMediaPlayer;

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
    public void init(Context context) {
        mContext = context;
    }

    /**
     * Start playing associated resource.
     *
     * @param resourceId Resource.
     */
    @SuppressWarnings("unused")
    public void startMusic(int resourceId) {
        initializePlayer();

        try {
            AssetFileDescriptor assetFileDescriptor = mContext.getResources()
                    .openRawResourceFd(resourceId);

            mMediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getDeclaredLength());

            assetFileDescriptor.close();
        }
        catch (IllegalArgumentException ignored) { }
        catch (IllegalStateException    ignored) { }
        catch (IOException              ignored) { }

        mMediaPlayer.setLooping(true);
        mMediaPlayer.setVolume(100, 100);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.start();
            }
        });
    }

    /**
     * Stop playing music.
     */
    @SuppressWarnings("unused")
    public void stopMusic() {

        initializePlayer();
    }

    /**
     * Pause music.
     */
    @SuppressWarnings("unused")
    public void pauseMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    /**
     * Resume music.
     */
    @SuppressWarnings("unused")
    public void resumeMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Initialize the player.  First make sure
     * that it is released.
     */
    private void initializePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }

        mMediaPlayer = new MediaPlayer();
    }
}
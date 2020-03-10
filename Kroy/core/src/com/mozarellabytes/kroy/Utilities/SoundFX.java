package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mozarellabytes.kroy.Screens.DanceScreen;
import com.mozarellabytes.kroy.Screens.DifficultyScreen;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Screens.MenuScreen;

/**
 * Sound Effects Manager for all in-game audio, accessed via static context.
 * All Music and SoundFX can be enabled/disabled with the StopMusic/PlayMusic.
 */

public class SoundFX {

    /** Used only for the truck attacking sound. True if it is playing, false if it isn't */
    public static boolean isPlaying = false;

    /** All sounds can be played when this is true, else no sound will play */
    public static boolean music_enabled = false;

    public static final Music sfx_menu = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.mp3"));
    public static final Music sfx_soundtrack = Gdx.audio.newMusic(Gdx.files.internal("sounds/soundtrack.mp3"));
    public static final Music sfx_danceoff = Gdx.audio.newMusic(Gdx.files.internal("sounds/140bpm.mp3"));

    public static final Sound sfx_truck_attack = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_attack.wav"));
    public static final Sound sfx_truck_damage = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_damage.wav"));
    public static final Sound sfx_truck_spawn = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_spawn.wav"));
    public static final Sound sfx_fortress_destroyed = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/fortress_destroyed.wav"));
    public static final Sound sfx_fortress_attack = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/fortress_attack.wav"));
    public static final Sound sfx_pause = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/pause.wav"));
    public static final Sound sfx_unpause = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/unpause.wav"));
    public static final Sound sfx_horn = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/horn.mp3"));
    public static final Sound sfx_button_clicked = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/button_clicked.wav"));
    public static final Sound sfx_kick = Gdx.audio.newSound(Gdx.files.internal("sounds/kick.wav"));
    public static final Sound sfx_snare = Gdx.audio.newSound(Gdx.files.internal("sounds/snare.wav"));


    /** Plays attacking sound for FireTrucks only if it isn't already playing */
    public static void playTruckAttack() {
        if (!isPlaying) {
            sfx_truck_attack.loop();
            sfx_truck_attack.play();
            isPlaying = true;
        }
    }

    /** Stops the sound of FireTrucks attacking and resets isPlaying to false */
    public static void stopTruckAttack() {
        if (isPlaying) {
            sfx_truck_attack.stop();
            isPlaying = false;
        }
    }

    public static void decideMusic(Screen screen) {
        if (music_enabled) {
            if (screen instanceof GameScreen) {
                sfx_danceoff.stop();
                sfx_menu.stop();
                sfx_soundtrack.play();
                sfx_soundtrack.setVolume(0.5f);
                sfx_soundtrack.setLooping(true);
            } else if (screen instanceof MenuScreen || screen instanceof DifficultyScreen) {
                sfx_danceoff.stop();
                sfx_soundtrack.pause();
                sfx_menu.play();
                sfx_menu.setVolume(0.5f);
                sfx_menu.setLooping(true);
            } else if (screen instanceof DanceScreen) {
                sfx_soundtrack.pause();
                sfx_menu.stop();
                sfx_danceoff.play();
                sfx_danceoff.setVolume(0.5f);
                sfx_danceoff.setLooping(true);
            }
        } else {
            sfx_danceoff.setVolume(0);
            sfx_menu.setVolume(0);
            sfx_soundtrack.setVolume(0);
        }
    }

    public static void toggleMusic(Screen screen) {
        music_enabled = !music_enabled;
        decideMusic(screen);
    }
    /** Plays danceoff music */
    public static void playDanceoffMusic() {
        sfx_danceoff.setLooping(true);
        sfx_danceoff.play();
    }

}

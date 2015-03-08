package org.c1.sound;

import org.c1.level.*;

public interface IAudioHandler
{

    void playSound(String id, Level w, float x, float y, float z);

    void playSound(String id, ILocatable location);

    void playSound(Sound sound);

    void playMusic(String id, float volume);

    void playMusic(Music music);
}

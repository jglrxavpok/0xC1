package org.c1.client.sound;

import java.util.*;

import com.google.common.collect.*;

import org.c1.client.*;
import org.c1.level.*;
import org.c1.maths.*;
import org.c1.sound.*;

public class DirectSoundProducer implements IAudioHandler
{

    public static final String BACKGROUND_MUSIC = "backMusic";
    //private SoundSystem        sndSystem;
    private List<Sound>        playingSounds    = Lists.newArrayList();

    @Override
    public void playSound(String id, Level w, float x, float y, float z)
    {
        playSound(new Sound(AudioRegistry.getRandom(id), 1, w, x, y, z));
    }

    @Override
    public void playSound(Sound sound)
    {
        //sndSystem.newStreamingSource(false, sound.getSourceName(), sound.getURL(), sound.getFileIdentifier(), false, sound.getPosX(), sound.getPosY(), sound.getPosZ(), 0, 0);
        //sndSystem.setVolume(sound.getSourceName(), sound.getVolume());
        //sndSystem.setPitch(sound.getSourceName(), sound.getPitch());
        //sndSystem.play(sound.getSourceName());
        playingSounds.add(sound);
    }

    public void playMusic(Music music)
    {
        playMusicWithID(music, music.getID());
    }

    public void playMusic(String id, float volume)
    {
        playMusic(new Music(AudioRegistry.getRandom(id), volume));
    }

    public void playBackgroundMusic(Music music)
    {
        playMusicWithID(music, BACKGROUND_MUSIC);
    }

    private void playMusicWithID(Music music, String id)
    {
        //sndSystem.newStreamingSource(true, id, music.getURL(), music.getFileIdentifier(), false, 0, 0, 0, SoundSystemConfig.ATTENUATION_ROLLOFF, 0);
        //sndSystem.setVolume(id, music.getVolume());
        //sndSystem.setPitch(id, music.getPitch());
        //sndSystem.play(id);
    }

    public void setListenerOrientation(Quaternion q)
    {

    }

    public void setListenerLocation(ILocatable loc)
    {
        setListenerLocation(loc.getX(), loc.getY(), loc.getZ());
    }

    public void setListenerLocation(float x, float y, float z)
    {
    }

    public void playBackgroundMusic(String string)
    {
        playBackgroundMusic(AudioRegistry.getFirst(string));
    }

    public void playBackgroundMusic(AudioInfo audio)
    {
        playBackgroundMusic(new Music(audio, C1Client.getClient().getGameSettings().musicVolume.getValue()));
    }

    public boolean isMusicPlaying()
    {
        return /*sndSystem.playing(BACKGROUND_MUSIC)*/false;
    }

    public void setSoundVolume(float volume)
    {
        List<Sound> toRemove = Lists.newArrayList();
        for(int i = 0; i < playingSounds.size(); i++ )
        {
            Sound playing = playingSounds.get(i);
            if(playing == null)
            {
                continue;
            }
            //sndSystem.setVolume(playing.getSourceName(), volume);
            if(!isPlaying(playing.getSourceName()))
            {
                toRemove.add(playing);
            }
        }
        //playingSounds.removeAll(toRemove);
    }

    public boolean isPlaying(String sourceName)
    {
        return false; //sndSystem.playing(sourceName);
    }

    public void setMusicVolume(float volume)
    {
        //sndSystem.setVolume(BACKGROUND_MUSIC, volume);
    }

    public void setMasterVolume(float volume)
    {
        //sndSystem.setMasterVolume(volume);
    }

    @Override
    public void playSound(String id, ILocatable location)
    {
        playSound(id, location.getLevel(), location.getX(), location.getY(), location.getZ());
    }

    public void cleanUp()
    {
        //sndSystem.cleanup();
    }

}

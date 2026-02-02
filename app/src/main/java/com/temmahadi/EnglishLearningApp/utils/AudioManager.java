package com.temmahadi.EnglishLearningApp.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class AudioManager {
    private static final String TAG = "AudioManager";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String currentRecordingPath;
    private OnPlaybackCompleteListener playbackCompleteListener;
    
    public interface OnPlaybackCompleteListener {
        void onPlaybackComplete();
    }
    
    public AudioManager() {
    }
    
    public void setOnPlaybackCompleteListener(OnPlaybackCompleteListener listener) {
        this.playbackCompleteListener = listener;
    }
    
    public boolean startRecording(Context context, String fileName) {
        try {
            // Create recordings directory
            File recordingsDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "recordings");
            if (!recordingsDir.exists()) {
                recordingsDir.mkdirs();
            }
            
            // Create file path
            currentRecordingPath = new File(recordingsDir, fileName + ".3gp").getAbsolutePath();
            
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(currentRecordingPath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            
            mediaRecorder.prepare();
            mediaRecorder.start();
            
            Log.d(TAG, "Recording started: " + currentRecordingPath);
            return true;
            
        } catch (IOException e) {
            Log.e(TAG, "Recording failed: " + e.getMessage());
            return false;
        }
    }
    
    public String stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                Log.d(TAG, "Recording stopped: " + currentRecordingPath);
                return currentRecordingPath;
            } catch (RuntimeException e) {
                Log.e(TAG, "Stop recording failed: " + e.getMessage());
            }
        }
        return null;
    }
    
    public boolean startPlayback(String filePath) {
        try {
            if (mediaPlayer != null) {
                stopPlayback();
            }
            
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            
            mediaPlayer.setOnCompletionListener(mp -> {
                stopPlayback();
                if (playbackCompleteListener != null) {
                    playbackCompleteListener.onPlaybackComplete();
                }
            });
            
            mediaPlayer.start();
            
            Log.d(TAG, "Playback started: " + filePath);
            return true;
            
        } catch (IOException e) {
            Log.e(TAG, "Playback failed: " + e.getMessage());
            return false;
        }
    }
    
    public void stopPlayback() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d(TAG, "Playback stopped");
        }
    }
    
    public boolean isRecording() {
        return mediaRecorder != null;
    }
    
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
    
    public void release() {
        stopRecording();
        stopPlayback();
    }
}

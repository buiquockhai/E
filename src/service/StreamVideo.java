package service;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class StreamVideo {
	
	public ImageView imgPause;
	public ImageView imgPlay;
	
	private int res;
	private boolean pause;
	
	private Button btnTmp;
	
	private MediaView media;
	
	private MediaPlayer mediaPlayer;
	
	private List<String> listURL = new ArrayList<String>();
	
	public StreamVideo(MediaView media) {
		super();
		this.media = media;
		setStream();
	}

	public void loadURL(int flag) {
		listURL.clear();
		if(flag==1) {
			listURL.add("file:/C:/temp/video/AESencryption.mp4");
			listURL.add("file:/C:/temp/video/AESEncryption2.mp4");
		}
	}
	
	public void stream(String url) {
		if(mediaPlayer!=null) {
			mediaPlayer.dispose();
			mediaPlayer = null;
		}
		mediaPlayer = new MediaPlayer(new Media(url));
		mediaPlayer.setAutoPlay(true);
		media.setMediaPlayer(mediaPlayer);
	}
	
	public void setStream() {
		loadURL(1);
		stream(listURL.get(0));
		res = 0;
		pause = false;
	}
	
	public void nextVideo() {
		if(res<listURL.size()-1) {
			res++;
			stream(listURL.get(res));
		}
		pause = false;
		btnTmp.setVisible(false);
	}
	
	public void preVideo() {
		if(res>0) {
			res--;
			stream(listURL.get(res));
		}
		pause = false;
		btnTmp.setVisible(false);
	}

	public void runControl(Button btnRun) {
		btnTmp = btnRun;
		if(!pause) {
			btnRun.setVisible(true);
			mediaPlayer.pause();
			btnRun.setGraphic(imgPlay);
			pause = !pause;
		}
		else {
			btnRun.setVisible(false);
			mediaPlayer.play();
			btnRun.setGraphic(imgPause);
			pause = !pause;
		}
	}
}




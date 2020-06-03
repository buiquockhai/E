package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import service.AES;
import service.AlgoType;
import service.Hash;
import service.Ideas;
import service.StreamVideo;

public class Controller implements Initializable{

	private StreamVideo streamVideo;
	private AES aes;
	private File rtFile;
	private File fIn;
	private File fOut;
	
	@FXML
	private AnchorPane pane;
	@FXML
	private MediaView media;
	@FXML
	private Button btnNext;
	@FXML
	private Button btnPre;
	@FXML
	private Button btnRun;
	@FXML
	private Button btnCopA;
	@FXML
	private Button btnCopB;
	@FXML
	private TextArea txtArA;
	@FXML
	private TextArea txtArB;
	@FXML
	private TextArea txtHashIn;
	@FXML
	private TextArea txtHashOut;
	@FXML
	private TextField txtPrivate;
	@FXML
	private Button btnChoose;
	@FXML
	private Button btnExp;
	@FXML
	private TextField txtInputFile;
	@FXML
	private TextField txtOutputFile;
	@FXML
	private TextField txtNameIn;
	@FXML
	private TextField txtNameOut;
	@FXML
	private Label lbConv;
	@FXML
	private Label lbHash;
	@FXML
	private Button btnLeft;
	@FXML
	private Button btnRight;
	@FXML
	private Button btnIconInput;
	@FXML
	private Button btnIconOutput;
	@FXML
	private Button btnHashNext;
	@FXML
	private Button btnHashPre;
	@FXML
	private Button btnAdd1;
	@FXML
	private Button btnAdd2;
	@FXML
	private Button btnIconIn;
	@FXML
	private Button btnIconOut;
	@FXML
	private Button btnTxt;
	
	
	@FXML
	public void onClick_btnNext() {
		streamVideo.nextVideo();	
	}
	
	@FXML
	public void onClick_btnPre() {
		streamVideo.preVideo();
	}
	
	@FXML
	public void onMouseClick_media() {
		streamVideo.runControl(btnRun);
	}
	
	@FXML
	public void onClick_btnRun() {
		
	}
	
	@FXML
	public void onClick_btnCopA() {
		txtArA.setText("");
	}

	@FXML
	public void onClick_btnCopB() {
		if(txtArB.getText().isEmpty()) return;
		Clipboard clipboard = Clipboard.getSystemClipboard();
		ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(txtArB.getText());
		clipboard.setContent(clipboardContent);
	}

	@FXML
	public void keyEnter_txtArA(KeyEvent event) {
		
		String privateKey = txtPrivate.getText().trim();
		
		if(privateKey.isEmpty()||privateKey.length()!=16) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Key value is wrong type!");
			alert.showAndWait();
			return;
		}
		
		if(event.getCode() == KeyCode.ENTER) {
			String plain = txtArA.getText().trim();
			String cipher;
			if(privateKey.isEmpty()||plain.isEmpty()) return;
			aes = new AES(privateKey);
			try {
				if(AlgoType.isEn()) cipher = (String) aes.encrypt("C:\\temp\\files\\",plain,true);
				else cipher = (String) aes.decrypt("C:\\temp\\deFile\\",plain,true);
				txtArB.setText(cipher);
			} catch (Exception e) {
				e.printStackTrace();
			}
	     }
	}

	@SuppressWarnings("unused")
	@FXML
	public void onClick_btnChoose() {
		
		String privateKey = txtPrivate.getText().trim();
		Icon icon = null;
		
		if(privateKey.isEmpty()||privateKey.length()!=16) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Key value is wrong type!");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("arrow.png")));
			alert.showAndWait();
			return;
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText(null);
		alert.setContentText("Choose your encrypt mode!");
		((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("File");
		ButtonType folderBtn = new ButtonType("Folder", ButtonData.YES);
		alert.getDialogPane().getButtonTypes().add(folderBtn);
		
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK) {
			
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(null);
			if(file==null) return;
			
			FileChooser fileChooserSave = new FileChooser();
			fileChooserSave.setTitle("Save");
			
			fileChooserSave.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("text file", "*.txt","*.doc","*.docx"),
					new FileChooser.ExtensionFilter("pdf", "*.pdf"),
					new FileChooser.ExtensionFilter("image", "*.png","*.jpg","*.gif"));
			File saveFile = fileChooserSave.showSaveDialog(null);
			if(saveFile==null) return;
			
			String path = saveFile.getAbsolutePath().toString().substring(0,saveFile.getAbsolutePath().length()-4) + file.getName().substring(file.getName().length()-4,file.getName().length());
			
			aes = new AES(privateKey);
			txtInputFile.setText(file.getName());
			try {
				if(AlgoType.isEn()) rtFile = (File) aes.encrypt(path,(File)file,true);
				else rtFile = (File) aes.decrypt(path,(File)file,true);
				txtOutputFile.setText(rtFile.getName());
				 icon = FileSystemView.getFileSystemView().getSystemIcon(rtFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(action.get() == folderBtn) {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			Stage stage = (Stage) pane.getScene().getWindow();
			File folder = directoryChooser.showDialog(stage);
			if(folder==null) return;
			txtInputFile.setText(folder.getName());
			aes = new AES(privateKey);
			
			try {
				if(AlgoType.isEn()) aes.folderEcrypt("C:\\temp\\files\\",folder);
				else aes.folderDecrypt("C:\\temp\\deFile\\",folder);
				txtOutputFile.setText(folder.getName());
				icon = FileSystemView.getFileSystemView().getSystemIcon(folder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else { return; }
		
		
		BufferedImage bufferedImage = Ideas.iconToImage(icon);
		Image img = SwingFXUtils.toFXImage(bufferedImage, null);
		
		ImageView imgViewIcon = new ImageView(img);
		imgViewIcon.setFitWidth(12);
		imgViewIcon.setFitHeight(12);
		btnIconInput.setGraphic(imgViewIcon);
		
		ImageView imageViewIcon1 = new ImageView(img);
		imageViewIcon1.setFitWidth(12);
		imageViewIcon1.setFitHeight(12);
		btnIconOutput.setGraphic(imageViewIcon1);
		
		
		
	}
	
	public void onClick_conv() {
		AlgoType.setEn(!AlgoType.isEn());
		if(AlgoType.isEn()) lbConv.setText("encryption");
		else lbConv.setText("decryption");
	}
	
	
	@FXML
	public void onClick_btnExp() {
		
		if(txtOutputFile.getText().trim().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("No file is found!");
			alert.showAndWait();
			return;
		}
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save as");
		
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("text file", "*.txt","*.doc","*.docx"),
				new FileChooser.ExtensionFilter("pdf", "*.pdf"),
				new FileChooser.ExtensionFilter("image", "*.png","*.jpg","*.gif"));
		try {
			File saveAsFile;
			File filePath = fileChooser.showSaveDialog(null);
			String rtFileName = rtFile.getName();
//			System.out.print(filePath.getAbsolutePath());
			saveAsFile = new File(filePath.getAbsolutePath().toString().substring(0,filePath.getAbsolutePath().length()-4) + rtFileName.substring(rtFileName.length()-4,rtFileName.length()));
			Files.copy(rtFile.toPath(), saveAsFile.toPath());
		} catch (Exception e) {}
		
	}
	
	
	@FXML
	public void onClick_Conv() {
		
		AlgoType.setAlgoType(!AlgoType.getAlgoType());
		if(AlgoType.getAlgoType()) lbHash.setText("MD5");
		else lbHash.setText("SHA-256");
		
		String hashIn = txtHashIn.getText();
		String hashOut = txtHashOut.getText();
		try {
			if(!hashIn.isEmpty()) {
				if(AlgoType.getAlgoType()) txtHashIn.setText(Hash.Hash(fIn, "MD5"));
				else txtHashIn.setText(Hash.Hash(fIn, "SHA-256"));
			}
			if(!hashOut.isEmpty()) {
				if(AlgoType.getAlgoType()) txtHashOut.setText(Hash.Hash(fOut, "MD5"));
				else txtHashOut.setText(Hash.Hash(fOut, "SHA-256"));
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		if(!hashIn.isEmpty()&&!hashOut.isEmpty()) isMatch(hashIn, hashOut);
		
	}
	
	
	@FXML
	public void onClick_btnAdd1() {
		FileChooser fileChooser = new FileChooser();
		fIn = fileChooser.showOpenDialog(null);
		
		if(fIn==null) return;
		
		txtNameIn.setText(fIn.getName());
		
		Icon icon = FileSystemView.getFileSystemView().getSystemIcon(fIn);
		BufferedImage bufferedImage = Ideas.iconToImage(icon);
		Image img = SwingFXUtils.toFXImage(bufferedImage, null);
		
		ImageView imgViewIcon = new ImageView(img);
		imgViewIcon.setFitWidth(12);
		imgViewIcon.setFitHeight(12);
		btnIconIn.setGraphic(imgViewIcon);
		
		
		try {
			if(AlgoType.getAlgoType()) txtHashIn.setText(Hash.Hash(fIn, "MD5"));
			else txtHashIn.setText(Hash.Hash(fIn, "SHA-256"));
		} catch (Exception e) { e.printStackTrace(); }
		
		String hashIn = txtHashIn.getText();
		String hashOut = txtHashOut.getText();
		if(!hashIn.isEmpty()&&!hashOut.isEmpty()) isMatch(hashIn, hashOut);
	}
	
	@FXML
	public void onClick_Add2() {
		FileChooser fileChooser = new FileChooser();
		fOut = fileChooser.showOpenDialog(null);
		
		if(fOut==null) return;
		
		txtNameOut.setText(fOut.getName());
		
		Icon icon = FileSystemView.getFileSystemView().getSystemIcon(fOut);
		BufferedImage bufferedImage = Ideas.iconToImage(icon);
		Image img = SwingFXUtils.toFXImage(bufferedImage, null);
		
		ImageView imgViewIcon = new ImageView(img);
		imgViewIcon.setFitWidth(12);
		imgViewIcon.setFitHeight(12);
		btnIconOut.setGraphic(imgViewIcon);
		
		try {
			if(AlgoType.getAlgoType()) txtHashOut.setText(Hash.Hash(fOut, "MD5"));
			else txtHashOut.setText(Hash.Hash(fOut, "SHA-256"));
		} catch (Exception e) { e.printStackTrace(); }
		
		String hashIn = txtHashIn.getText();
		String hashOut = txtHashOut.getText();
		if(!hashIn.isEmpty()&&!hashOut.isEmpty()) isMatch(hashIn, hashOut);
	}
	
	@FXML
	public void onClick_btnTxt() {
		FileChooser fileChooser = new FileChooser();
		fOut = fileChooser.showOpenDialog(null);
		
		if(fOut==null) return;
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(null);
		alert.setHeaderText(null);
		
//		Checking txt file
		String name = fOut.getName();
		if(!name.substring(name.length()-3, name.length()).equals("txt")) {
			alert.setContentText("Only matching with *.txt file");
			alert.showAndWait();
		}
		else {
			String key = "";
			try {
				FileReader fileReader = new FileReader(fOut);
				int i;
				while((i=fileReader.read()) != -1)
					key = key + (char)i;
			} catch (IOException e) {}
			if(key.length()!=16) {
				alert.setContentText("Content of key file must have 16 character!");
				alert.show();
			}
			else {
				txtPrivate.setText(key);
			}
		}
		
	}
	
	public void startStatus() {
//		Start with AES aglo by default
		AlgoType.setAlgoType(true);
		AlgoType.setEn(true);
//		Set for btnRun
		Image imgPause = new Image(getClass().getResourceAsStream("multimedia.png"));
		streamVideo.imgPause = new ImageView(imgPause);
		streamVideo.imgPause.setFitWidth(15);
		streamVideo.imgPause.setFitHeight(15);
		Image imgPlay= new Image(getClass().getResourceAsStream("play.png"));
		streamVideo.imgPlay = new ImageView(imgPlay);
		streamVideo.imgPlay.setFitWidth(15);
		streamVideo.imgPlay.setFitHeight(15);
		
		btnRun.setGraphic(streamVideo.imgPause);
		btnRun.setVisible(false);
		
//		Set for btnCop
		Image imgCop = new Image(getClass().getResourceAsStream("trash.png"));
		ImageView imageViewCop = new ImageView(imgCop);
		imageViewCop.setFitHeight(15);
		imageViewCop.setFitWidth(15);
		btnCopA.setGraphic(imageViewCop);
		
		Image imgDel = new Image(getClass().getResourceAsStream("copy.png"));
		ImageView imageViewCop1 = new ImageView(imgDel);
		imageViewCop1.setFitHeight(15);
		imageViewCop1.setFitWidth(15);
		btnCopB.setGraphic(imageViewCop1);
		
		Image imgAdd = new Image(getClass().getResourceAsStream("plus.png"));
		ImageView imgViewAdd = new ImageView(imgAdd);
		imgViewAdd.setFitWidth(12);
		imgViewAdd.setFitHeight(12);
		btnChoose.setGraphic(imgViewAdd);
		
		ImageView imgViewAdd3 = new ImageView(imgAdd);
		imgViewAdd3.setFitWidth(12);
		imgViewAdd3.setFitHeight(12);
		btnTxt.setGraphic(imgViewAdd3);
		
		ImageView imgViewAdd1 = new ImageView(imgAdd);
		imgViewAdd1.setFitWidth(12);
		imgViewAdd1.setFitHeight(12);
		btnAdd1.setGraphic(imgViewAdd1);
		
		ImageView imgViewAdd2 = new ImageView(imgAdd);
		imgViewAdd2.setFitWidth(12);
		imgViewAdd2.setFitHeight(12);
		btnAdd2.setGraphic(imgViewAdd2);
		
		Image imgSave = new Image(getClass().getResourceAsStream("save.png"));
		ImageView imgViewSave = new ImageView(imgSave);
		imgViewSave.setFitWidth(12);
		imgViewSave.setFitHeight(12);
		btnExp.setGraphic(imgViewSave);
		
	}
	
	public void isMatch(String a, String b) {
		if(a.equals(b)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Matching!");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("arrow.png")));
			
			alert.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("No matching!");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResourceAsStream("arrow.png")));
			alert.showAndWait();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		streamVideo = new StreamVideo(media);
		startStatus();
	}

}

package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("sample.fxml"));
		loader.setController(new Controller());
		Parent root = loader.load();
//		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("");
		primaryStage.setScene(new Scene(root,870,600));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("arrow.png")));
		primaryStage.show();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);

		
	}
}

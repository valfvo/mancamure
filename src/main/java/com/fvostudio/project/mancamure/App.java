package com.fvostudio.project.mancamure;

// import java.io.IOException;
// import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

// import javafx.application.Application;
// import javafx.concurrent.Worker;
// import javafx.scene.Scene;
// import javafx.scene.web.WebEngine;
// import javafx.scene.web.WebView;
// import javafx.stage.Stage;
// import netscape.javascript.JSObject;

// public class App extends Application {
public class App {
    // public TestCallback test = new TestCallback();
    // public Thread gameThread = null;

    // @Override
    // public void start(Stage primaryStage) throws IOException {
    //     gameThread = new Thread(new Awale(getParameters().getRaw()));
    //     gameThread.start();
    //     // System.setProperty("prism.lcdtext", "false");  // anti-aliased text

    //     WebView webView = new WebView();
    //     // webView.setZoom(3);
    //     WebEngine webEngine = webView.getEngine();

    //     String path = System.getProperty("user.dir") + "/src/main/html/index.html";
    //     String url = Path.of(path).toUri().toURL().toString();
    //     System.out.println(url);

    //     webEngine.getLoadWorker().stateProperty()
    //         .addListener((observable, oldValue, newValue) -> {
    //             if (newValue != Worker.State.SUCCEEDED) {
    //                 return;
    //             }
    //             JSObject window = (JSObject) webEngine.executeScript("window");
    //             window.setMember("test", test);
    //             System.out.println("loaded");
    //         }
    //     );
    //     webEngine.load(url);

    //     Scene scene = new Scene(webView, 1280, 720);

    //     primaryStage.setTitle("Mancamure");
    //     primaryStage.setScene(scene);
    //     primaryStage.show();
    //     // primaryStage.setMaximized(true);
    // }

    public static void main(String[] args) {
        // launch(args);
        Awale game = new Awale(List.of(args));
        game.run();

        System.out.println("Press enter to exit...");
        Scanner scan = new Scanner(System.in);
        scan.nextLine();
        scan.close();
    }
}

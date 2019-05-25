import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class test {
    public static void main(String[] args) {
        JFileChooser fileChooserDialogue =  new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooserDialogue.setName("Choose A File");
        int response = fileChooserDialogue.showOpenDialog(null);
        String absPath = "",parentDIR="",filename="";
        if(response == JFileChooser.APPROVE_OPTION) {
//            System.out.println(response + "\t" + fileChooserDialogue.getSelectedFile().getAbsolutePath());
            absPath = fileChooserDialogue.getSelectedFile().getAbsolutePath();
            parentDIR = fileChooserDialogue.getSelectedFile().getParent();
            filename = fileChooserDialogue.getSelectedFile().getName().split("[.]")[0];
            println(absPath);
            println(parentDIR);
            println(filename);
            fileChooserDialogue.showSaveDialog(null);
            absPath = fileChooserDialogue.getSelectedFile().getAbsolutePath();
            parentDIR = fileChooserDialogue.getSelectedFile().getParent();
            filename = fileChooserDialogue.getSelectedFile().getName();
            println(absPath);
            println(parentDIR);
            println(filename);
        }else
            System.out.println("Nope");
//        if (selectedFile != null) {
//            System.out.println("error");
//        }
    }
    private static void println(Object o){
        System.out.println(o);
    }
}

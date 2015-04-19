/*
 * Copyright 2014 roylisto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Controller;

import GUI.MainForm;
import Video.AerialVideo;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

/**
 *
 * @author roylisto
 */
public class ConvertDirButtonListener extends MainListener{

    public ConvertDirButtonListener(MainForm form, AerialVideo video) {
        super(form, video);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
             
            FileDialog fd = new FileDialog(theForm, "Set Files", FileDialog.SAVE);
            fd.setVisible(true);
            String filename = fd.getFile();
            if (filename == null)
              System.out.println("You cancelled the choice");
            else
                theVideo.setConvertedVideoDir(fd.getDirectory()+ fd.getFile());                                        
                theForm.setConvertedFileDir(theVideo.getConvertedVideoDir());
            }catch(Exception ex){
                theForm.displayError(ex.toString());
            }
    }
    
}

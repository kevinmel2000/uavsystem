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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;

/**
 *
 * @author roylisto
 */
public class UAVSystemConfig {
    private MainForm theForm;
    private AerialVideo theVideo;        
    public UAVSystemConfig(MainForm form,AerialVideo video){
        this.theForm=form;
        this.theVideo=video;
        this.theForm.addOpenFileListener(new OpenFileListener(theForm,theVideo));
        this.theForm.addConvertDirectoryListener(new ConvertDirButtonListener(theForm, theVideo));
        this.theForm.addConvertedButtonListener(new ConvertedButtonListener(theForm, theVideo));
        this.theForm.addSetNameListener(new SetFileNameButtonListener(theForm, theVideo));
        this.theForm.addMosaicButtonListener(new MosaicButtonListener(theForm, theVideo));
    }        
}
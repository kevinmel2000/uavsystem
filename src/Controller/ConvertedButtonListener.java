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
import Converter.VideoConverter;

/**
 *
 * @author roylisto
 */
public class ConvertedButtonListener extends MainListener{

    public ConvertedButtonListener(MainForm form, AerialVideo video) {
        super(form, video);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       try{
           if(!theForm.getFileDir().equals("")){
               if(!theForm.getConvertedFileDir().equals("")){
                VideoConverter theConverter=new VideoConverter(theVideo.getVideoDir(), theVideo.getConvertedVideoDir(),this);
                theConverter.convertToMjpeg();
               }else{
                   theForm.displayError("File Dir Field or Converted Dir field can't be empty");
               }
           }else{
               theForm.displayError("File Dir Field or Converted Dir field can't be empty");
           }
           
       }catch(Exception ex){
           theForm.displayError(ex.toString());
       }
    }         
    public void setCommandOutput(String output){
        theForm.setCommandOutput(output+"\n");
    }
}

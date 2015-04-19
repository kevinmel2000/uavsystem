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
package Main;
import Controller.UAVSystemConfig;
import GUI.MainForm;
import Video.AerialVideo;
import javax.swing.JFrame;
/**
 *
 * @author roylisto
 */
public class MainSystem {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
       // myMainForm=new MainForm();
        MainForm theMainForm=new MainForm();
        AerialVideo theVideo=new AerialVideo();
        UAVSystemConfig theController=new UAVSystemConfig(theMainForm, theVideo);
        theMainForm.setVisible(true);
        theMainForm.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
}

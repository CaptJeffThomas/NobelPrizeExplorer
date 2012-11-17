/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cmput305.group.project;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author Charles Logan
 */
public class ImageURLLoader extends SwingWorker<Icon, Object> {
    public enum imageCache {

        IMAGE;
        private Map<String, Icon> imageMap;

        private imageCache() {
            imageMap = new HashMap<String, Icon>();
        }

        public void cacheImage(String irl, Icon icon) {
            imageMap.put(irl, icon);
        }

        public Icon imageCheck(String url) {
            return imageMap.get(url);
        }
    }
    private String URL;
    private JLabel image;

    public ImageURLLoader(JLabel imageLabel, String URL) {
        this.image = imageLabel;
        this.URL = URL;
    }

    @Override
    protected Icon doInBackground() throws Exception {
        //System.out.println("irl to imageicon...");
        Icon temp = null;
        
        temp = imageCache.IMAGE.imageCheck(URL);
        if (temp != null){
            return temp;
        }
        
        try {
            temp = (new ImageIcon(new URL(URL)));
        } catch (IOException ex) {
                Logger.getLogger(ImageURLLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        
        imageCache.IMAGE.cacheImage(URL,temp);
        return temp;
    }

    
    protected void done() {

        try {
            //System.out.println("loaded image");
            image.setIcon(get());
        } catch (InterruptedException ex) {
            System.out.println(ex);
        } catch (ExecutionException ex) {
            System.out.println(ex);
        }
    }
}
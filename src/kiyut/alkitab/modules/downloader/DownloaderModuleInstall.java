package kiyut.alkitab.modules.downloader;

import org.crosswire.jsword.util.WebWarning;
import org.openide.modules.ModuleInstall;

/**
 * DownloaderModuleInstall for Netbeans Platform
 * 
 */
public class DownloaderModuleInstall extends ModuleInstall {
    @Override
    public  void restored() {
        super.restored();
        
        // always reset WebWarning
        WebWarning.instance().setShown(true);
        
        /*WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            public void run() {
                final Frame mainWindow = WindowManager.getDefault().getMainWindow();
                mainWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent evt) {
                    }
                });
            }
        });
         */

    }
}

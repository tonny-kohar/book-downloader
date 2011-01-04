package kiyut.alkitab.modules.downloader;

import java.util.logging.Level;
import java.util.logging.Logger;
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

        try {
            // always reset WebWarning
            WebWarning.instance().setShown(true);
        } catch (Exception ex) {
             Logger logger = Logger.getLogger(this.getClass().getName());
             logger.log(Level.FINE,ex.getMessage(),ex);
        }
    }
}

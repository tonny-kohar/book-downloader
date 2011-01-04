package kiyut.alkitab.modules.downloader.actions;

import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingUtilities;
import org.crosswire.bibledesktop.book.install.InternetWarning;
import org.crosswire.bibledesktop.book.install.SitesPane;
import org.crosswire.jsword.book.Books;
import org.crosswire.jsword.book.BooksEvent;
import org.crosswire.jsword.book.BooksListener;
import org.crosswire.jsword.util.WebWarning;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;

/**
 * Action that will open downloader panel.
 * It delegates into org.crosswire.bibledesktop.book.install.SitesPane
 * 
 */
public class DownloaderAction extends CallableSystemAction {

    private boolean booksChanged;
    private BooksListener booksListener;
    
    public DownloaderAction() {
        booksChanged = false;
        booksListener = new BooksListener() {
            @Override
            public void bookAdded(BooksEvent arg0) {
                booksChanged = true;
            }

            @Override
            public void bookRemoved(BooksEvent arg0) {
                booksChanged = true;
            }
        };
    }
    
    @Override
    public void performAction() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (WebWarning.instance().isShown()) {
                    int choide = InternetWarning.showDialog(WindowManager.getDefault().getMainWindow(), "?");
                    if (choide != InternetWarning.GRANTED) {
                        return;
                    }
                }
                
                SitesPane sitesPane = new SitesPane();
                sitesPane.showInDialog(WindowManager.getDefault().getMainWindow());
                
                // XXX below is hack, should ask JSword Dev for proper API
                
                Window window = SwingUtilities.getWindowAncestor(sitesPane);
                if (window == null) { return; }
                
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent evt) {
                        Books.installed().addBooksListener(booksListener);
                    }

                    /*@Override
                    public void windowClosing(WindowEvent evt) {
                        // why this is not called
                        System.out.println("WindowClosing called: " + booksChanged);
                    }*/
                }); 
                
                window.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentHidden(ComponentEvent evt) {
                        Books.installed().removeBooksListener(booksListener);
                        if (booksChanged) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    confirmRestart();
                                }
                            });
                        }
                    }
                });
            } 
        });
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(DownloaderAction.class, "CTL_DownloaderAction.Text");
    }

    /* @Override
    protected String iconResource() {
        return NbBundle.getMessage(NewViewAction.class, "ICON_DownloaderAction");
    } */
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    private void confirmRestart() {
        String msg = NbBundle.getMessage(DownloaderAction.class, "MSG_ConfirmRestart.Text");
        NotifyDescriptor confirm = new NotifyDescriptor.Confirmation(msg, NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.QUESTION_MESSAGE);
        Object result = DialogDisplayer.getDefault().notify(confirm);
        if (NotifyDescriptor.YES_OPTION.equals(result)) {
            LifecycleManager.getDefault().exit();
        }
    }
}

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOResourceManager;

public class NavigationPage extends MonitorComponent {
	private static final long serialVersionUID = 1L;
	private static final String _appBarImageName = "applications_bar.gif";
    private static final String _hostBarImageName = "hosts_bar.gif";
    private static final String _siteBarImageName = "site_bar.gif";
    private static final String _prefBarImageName = "preferences_bar.gif";
    private static final String _helpBarImageName = "help_bar.gif";
    private static final String[] _barImageNames = new String[]{_appBarImageName, _hostBarImageName, _siteBarImageName, _prefBarImageName, _helpBarImageName};

    private static final String _appActiveImageName = "applications_tab_active.gif";
    private static final String _hostActiveImageName = "hosts_tab_active.gif";
    private static final String _siteActiveImageName = "site_tab_active.gif";
    private static final String _prefActiveImageName = "preferences_tab_active.gif";
    private static final String _helpActiveImageName = "help_tab_active.gif";

    private static final String _appInactiveImageName = "applications_tab_inactive.gif";
    private static final String _hostInactiveImageName = "hosts_tab_inactive.gif";
    private static final String _siteInactiveImageName = "site_tab_inactive.gif";
    private static final String _prefInactiveImageName = "preferences_tab_inactive.gif";
    private static final String _helpInactiveImageName = "help_tab_inactive.gif";

    String appleImageName = "AppleLogo.gif";
    String backgroundImageName = "background.gif";

    public int currentPage = APP_PAGE;
    
    public String barImageName() {
        return _barImageNames[currentPage];
    }

    public String appImageName() {
        return (currentPage == APP_PAGE) ? _appActiveImageName : _appInactiveImageName;
    }

    public String hostImageName() {
        return (currentPage == HOST_PAGE) ? _hostActiveImageName : _hostInactiveImageName;
    }

    public String siteImageName() {
        return (currentPage == SITE_PAGE) ? _siteActiveImageName : _siteInactiveImageName;
    }

    public String prefImageName() {
        return (currentPage == PREF_PAGE) ? _prefActiveImageName : _prefInactiveImageName;
    }

    public String helpImageName() {
        return (currentPage == HELP_PAGE) ? _helpActiveImageName : _helpInactiveImageName;
    }


    public String backgroundImageSrc()  {
        WOResourceManager aResourceManager = application().resourceManager();
        return aResourceManager.urlForResourceNamed(backgroundImageName, null, null, context().request());
    }

    public WOComponent ApplicationsPageClicked() { return pageWithName("ApplicationsPage"); }
    public WOComponent HostsPageClicked() { return pageWithName("HostsPage"); }
    public WOComponent ConfigurePageClicked() { return pageWithName("ConfigurePage"); }
    public WOComponent PrefsPageClicked() { return pageWithName("PrefsPage"); }
    public WOComponent HelpPageClicked() { return pageWithName("HelpPage"); }
    
    public String versionString() {
    	String version = WOApplication.application().getWebObjectsVersion();
    	return (version != null) ? version : "5";
    }
    
}
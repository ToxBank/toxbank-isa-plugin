package net.toxbank.isa.creator.plugin;

import org.isatools.isacreator.plugins.host.service.PluginOntologyCVSearch;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 03/10/2011
 *         Time: 16:06
 */
public class Activator implements BundleActivator {



    public void start(BundleContext context) {

        Hashtable dict = new Hashtable();
        context.registerService(
                PluginOntologyCVSearch.class.getName(), new ToxBankRESTClient(), dict);
    }


    public void stop(BundleContext context) {
    }
}
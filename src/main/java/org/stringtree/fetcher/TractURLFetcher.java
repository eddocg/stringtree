package org.stringtree.fetcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.stringtree.Fetcher;
import org.stringtree.streams.StringStreamConverter;
import org.stringtree.streams.TractStreamConverter;
import org.stringtree.util.URLReadingUtils;

public class TractURLFetcher implements ContextSensitiveFetcher {
    static final Collection<SuffixDefinition> dflSuffices = Arrays.asList(new SuffixDefinition[] {
            new SuffixDefinition("tpl", new StringStreamConverter()), 
            new SuffixDefinition("tract", new TractStreamConverter())
        });
    
    private URL root;
    private Collection<SuffixDefinition> suffices;
    private Fetcher context;

    public TractURLFetcher(URL root, Collection<SuffixDefinition> suffices) {
        this.root = root;
        this.suffices = suffices;
    }

    public TractURLFetcher(URL root) {
        this(root, dflSuffices);
    }

    public TractURLFetcher(String root, Collection<SuffixDefinition> suffices) {
        try {
            this.root = URLReadingUtils.findURL(root, "file");
            this.suffices = suffices;
        } catch (IOException e) {
            e.printStackTrace();
            this.root = null;
        }
    }

    public TractURLFetcher(String root) {
        this(root, dflSuffices);
    }

    public Object getObject(String name) {
        Object ret = null;
        URL remote = null;
        Iterator<SuffixDefinition> tails = suffices.iterator();
        while (tails.hasNext()) {
            SuffixDefinition def = tails.next();
            try {
                remote = new URL(root, name+"." + def.getSuffix());
                ret = def.read(remote, context); // FIXME where should the real context come from
            } catch (MalformedURLException e) {
                /* don't complain, just ignore it or return null */
            } catch (IOException e) {
                /* don't complain, just ignore it or return null */
            }
        }
        return ret;
    }
    
    public String toString() {
        return "TractURLFetcher(" + root + ")";
    }

    public void setContext(Fetcher context) {
        this.context = context;
    }
}

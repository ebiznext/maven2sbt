package com.ebiznext.maven.plugin.display;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * 
 * @author stephane.manciot@ebiznext.com
 * @goal time
 */
public class TimeMojo extends AbstractMojo
{
    /**
     * Date format to use
     * 
     * @parameter expression="${display.timeFormat}" default-value="dd/MM/yyyy HH:mm:ss"
     * @required
     */
    protected String timeFormat;

    public void execute() throws MojoExecutionException
    {
        super.getLog().info(new SimpleDateFormat(timeFormat).format(new Date()));
    }

}

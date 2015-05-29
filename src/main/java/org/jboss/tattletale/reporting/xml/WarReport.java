/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.tattletale.reporting.xml;

import org.jboss.tattletale.Version;
import org.jboss.tattletale.core.NestableArchive;

import java.io.BufferedWriter;
import java.io.IOException;

import org.jboss.tattletale.reporting.abstracts.WarReportAbstract;
import org.jboss.tattletale.reporting.common.*;

/**
 * This type of report is to .war files as to {@link JarReport} is to .jar files.
 *
 * @author Navin Surtani
 */
public class WarReport extends WarReportAbstract
{
   /** DIRECTORY */
   private static final String DIRECTORY = "war";

   /** File name */
   private String fileName;

   /** The level of depth from the main output directory that this jar report would sit */
   private int depth;

   /**
    * Constructor
    *
    * @param nestableArchive - the war nestableArchive.
    */
   public WarReport(NestableArchive nestableArchive)
   {
      this(nestableArchive, 1);
   }

   /**
    * Constructor
    *
    * @param nestableArchive The nestableArchive
    * @param depth   The level of depth at which this report would lie
    */
   public WarReport(NestableArchive nestableArchive, int depth)
   {
      super (DIRECTORY, ReportSeverity.INFO, nestableArchive);
      StringBuffer sb = new StringBuffer(nestableArchive.getName());
      setFilename(sb.append(".xml").toString());
      this.depth = depth;
   }

   /**
    * Get the name of the directory
    *
    * @return The directory
    */
   @Override
   public String getDirectory()
   {
      return DIRECTORY;
   }

   /**
    * write the header of a html file.
    *
    * @param bw the buffered writer
    * @throws IOException if an error occurs
    */


   public void writeHead(BufferedWriter bw) throws IOException
   {
      if (depth == 1)
      {
         super.writeHead(bw);
      }
      else
      {
    	 bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?> "+ Dump.newLine());
         bw.write("<main>" + Dump.newLine());
         bw.write("<info>" + Dump.newLine());
         bw.write("  <title>" + Version.FULL_VERSION + ": " + getName() + "</title>" + Dump.newLine());
         
         for (int i = 1; i <= depth; i++)
         {
            bw.write("../");
         }
        
         bw.write("</info>" + Dump.newLine());

      }
   }

   /**
    * returns a war report specific writer.
    * war reports don't use a index.html but a html per archive.
    *
    * @return the BufferedWriter
    * @throws IOException if an error occurs
    */
   @Override
   protected BufferedWriter getBufferedWriter() throws IOException
   {
      return getBufferedWriter(getFilename());
   }
   private String getFilename()
   {
      return fileName;
   }

   private void setFilename(String fileName)
   {
      this.fileName = fileName;
   }
}

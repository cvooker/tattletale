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
package org.jboss.tattletale.reporting.abstracts;

import org.jboss.tattletale.Version;
import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.reporting.xml.Dump;
import org.jboss.tattletale.reporting.interfaces.Filter;
import org.jboss.tattletale.reporting.xml.KeyValueFilter;
import org.jboss.tattletale.reporting.common.ReportStatus;
import org.jboss.tattletale.reporting.interfaces.Report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedSet;

/**
 * Represents a report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 * @author Navin Surtani
 */
public abstract class AbstractReport implements Report
{
   /** Report id */
   private String id;

   /** The severity */
   protected int severity;

   /** The status */
   protected int status;

   /** The actions */
   protected SortedSet<Archive> archives;

   /** name of the report */
   private String name = null;

   /** output directory of the report */
   private String directory = null;

   /** output directory */
   private File outputDirectory;

   /** Filter */
   private String filter;

   /** Filter implementation */
   private Filter filterImpl;

   /** output filename */
   protected static final String INDEX = "index.xml";
 

   /**
    * Constructor
    *
    * @param id       The report id
    * @param severity The severity
    */
   public AbstractReport(String id, int severity)
   {
      this.id = id;
      this.severity = severity;
      this.status = ReportStatus.GREEN;
      this.filter = null;
      this.filterImpl = null;
   }

   /**
    * Constructor
    *
    * @param id        The report id
    * @param severity  The severity
    * @param name      The name of the report
    * @param directory The name of the output directory
    */
   public AbstractReport(String id, int severity, String name, String directory)
   {
      this(id, severity);
      this.name = name;
      this.directory = directory;
   }

   /**
    * Get the report id
    *
    * @return The value
    */
   public String getId()
   {
      return id;
   }

   /**
    * Get the severity
    *
    * @return The value
    */
   public int getSeverity()
   {
      return severity;
   }

   /**
    * Get the status
    *
    * @return The value
    */
   public int getStatus()
   {
      return status;
   }

   /**
    * Get the name of the directory
    *
    * @return The directory
    */
   public String getDirectory()
   {
      return directory;
   }

   /**
    * Get the name of the report
    *
    * @return The name
    */
   public String getName()
   {
      return name;
   }

   /**
    * Get the filter for the report
    *
    * @return The filter
    */
   public String getFilter()
   {
      return filter;
   }

   /**
    * Set the filter for the report
    *
    * @param filter The value
    */
   public void setFilter(String filter)
   {
      this.filter = filter;
      this.filterImpl = createFilter();
      this.filterImpl.init(filter);
   }

   /**
    * the output directory
    *
    * @return a file handle to the output directory
    */
   public File getOutputDirectory()
   {
      return outputDirectory;
   }

   /**
    * The name of the index file to be used. See {@link Report} for examples.
    *
    * @return name of the index file that is to contain Report data.
    */
   public String getIndexName()
   {
      return INDEX;
   }

   /**
    * Generate the report(s)
    *
    * @param outputDirectory The top-level output directory
    */
   public void generate(String outputDirectory)
   {
      try
      {/*
         createOutputDir(outputDirectory);
         BufferedWriter bw = getBufferedWriter();

         writeHead(bw);

         writeBodyHeader(bw);
         writeBodyContent(bw);
         writeBodyFooter(bw);

         writeFooter(bw);

         bw.flush();
         bw.close();*/
      }
      catch (Exception e)
      {
         System.err.println(getName() + " Report: " + e.getMessage());
         e.printStackTrace(System.err);
      }
   }

   /**
    * create the output directory
    *
    * @param outputDirectory the name of the directory
    */
   void createOutputDir(String outputDirectory)
   {
      this.outputDirectory = new File(outputDirectory, getDirectory());
      this.outputDirectory.mkdirs();
   }

   /**
    * get a default writer for writing to an index html file.
    *
    * @return a buffered writer
    * @throws IOException if an error occurs
    */
   protected BufferedWriter getBufferedWriter() throws IOException
   {
      return getBufferedWriter(INDEX);
   }

   /**
    * get a writer.
    *
    * @param filename the filename to use
    * @return a buffered writer
    * @throws IOException if an error occurs
    */
   public BufferedWriter getBufferedWriter(String filename) throws IOException
   {
      FileWriter fw = new FileWriter(getOutputDirectory().getAbsolutePath() + File.separator + filename);
      return new BufferedWriter(fw, 8192);
   }

   /**
    * Set the archives to be represented by this report
    *
    * @param archives The archives represented by this report
    */
   public void setArchives(SortedSet<Archive> archives)
   {
      this.archives = archives;
   }

   /**
    * write the header of a html file.
    *
    * @param bw the buffered writer
    * @throws IOException if an error occurs
    */
  
   /**
    * Comparable
    *
    * @param o The other object
    * @return The compareTo value
    */
   public int compareTo(Object o)
   {
      AbstractReport r = (AbstractReport) o;

      if (severity == r.getSeverity())
      {
         return getName().compareTo(r.getName());
      }
      else if (severity < r.getSeverity())
      {
         return -1;
      }
      else
      {
         return 1;
      }
   }

   /**
    * Equals
    *
    * @param obj The other object
    * @return True if equals; otherwise false
    */
   public boolean equals(Object obj)
   {
      if (obj == null || !(obj instanceof Report))
      {
         return false;
      }

      AbstractReport r = (AbstractReport) obj;

      return getName().equals(r.getName());
   }

   /**
    * Hash code
    *
    * @return The hash code
    */
   public int hashCode()
   {
      return 7 + 31 * getName().hashCode();
   }

   /**
    * Create filter
    *
    * @return The filter
    */
   protected Filter createFilter()
   {
      return new KeyValueFilter();
   }

   /**
    * Is filtered
    *
    * @return True if filtered; otherwise false
    */
   protected boolean isFiltered()
   {
      if (filterImpl != null)
      {
         return filterImpl.isFiltered();
      }

      return false;
   }

   /**
    * Is filtered
    *
    * @param archive The archive
    * @return True if filtered; otherwise false
    */
   protected boolean isFiltered(String archive)
   {
      if (filterImpl != null)
      {
         return filterImpl.isFiltered(archive);
      }

      return false;
   }

   /**
    * Is filtered
    *
    * @param archive The archive
    * @param query   The query
    * @return True if filtered; otherwise false
    */
   protected boolean isFiltered(String archive, String query)
   {
      if (filterImpl != null)
      {
         return filterImpl.isFiltered(archive, query);
      }

      return false;
   }
}

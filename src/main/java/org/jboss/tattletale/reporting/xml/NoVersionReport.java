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

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.core.Location;
import org.jboss.tattletale.core.NestableArchive;
import org.jboss.tattletale.reporting.abstracts.AbstractReport;
import org.jboss.tattletale.reporting.abstracts.NoVersionReportAbstract;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;

import org.jboss.tattletale.reporting.common.*;
import org.jboss.tattletale.reporting.interfaces.*;
/**
 * Multiple locations report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public class NoVersionReport extends NoVersionReportAbstract
{
   /** NAME */
   private static final String NAME = "No version";

   /** DIRECTORY */
   private static final String DIRECTORY = "noversion";

   /** Constructor */
  
   /**
    * write out the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    */
   public void writeBodyContent(BufferedWriter bw) throws IOException
   {
      bw.write("<elements>" + Dump.newLine());

     
      //bw.write("     <th>Name</th>" + Dump.newLine());
      //bw.write("     <th>Location</th>" + Dump.newLine());
  
      recursivelyWriteContent(bw, archives);
      bw.write("</elements>" + Dump.newLine());
   }

   private void recursivelyWriteContent(BufferedWriter bw, Collection<Archive> archives) throws IOException
   {
      boolean odd = true;

      for (Archive archive : archives)
      {

         if (archive instanceof NestableArchive)
         {
            NestableArchive nestableArchive = (NestableArchive) archive;
            recursivelyWriteContent(bw, nestableArchive.getSubArchives());
         }
         else
         {
            SortedSet<Location> locations = archive.getLocations();
            Iterator<Location> lit = locations.iterator();

            Location location = lit.next();

            boolean include = false;
            boolean filtered = isFiltered(archive.getName());

            while (!include && lit.hasNext())
            {
               location = lit.next();

               if (location.getVersion() == null)
               {
                  include = true;

                  if (!filtered)
                  {
                     status = ReportStatus.RED;
                  }
               }
            }

            if (include)
            {
               
                bw.write("  <element>" + Dump.newLine());
              
              
               bw.write("     <Name>../jar/" + archive.getName() + ".xml" +
                        archive.getName() + "</Name>" + Dump.newLine());
               bw.write("     <Location>");

               bw.write("       <table>" + Dump.newLine());

               lit = locations.iterator();
               while (lit.hasNext())
               {
                  location = lit.next();

                  bw.write("      <tr>" + Dump.newLine());

                  bw.write("        <td>" + location.getFilename() + "</td>" + Dump.newLine());
                  if (!filtered)
                  {
                     bw.write("        <td>");
                  }
                  else
                  {
                     bw.write("        <td>");
                  }
                  if (location.getVersion() != null)
                  {
                     bw.write(location.getVersion());
                  }
                  else
                  {
                     bw.write("<i>Not listed</i>");
                  }
                  bw.write("</td>" + Dump.newLine());

                  bw.write("      </tr>" + Dump.newLine());
               }

               bw.write("       </table>" + Dump.newLine());

               bw.write("</Location>" + Dump.newLine());
               bw.write("  </element>" + Dump.newLine());

               odd = !odd;
            }
         }
      }

   }

   /**
    * write out the header of the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    */
   public void writeBodyHeader(BufferedWriter bw) throws IOException
   {
      bw.write("<reporting>" + Dump.newLine());
      bw.write(Dump.newLine());

      bw.write("<h1>" + NAME + "</h1>" + Dump.newLine());

      bw.write("../index.xml" + Dump.newLine());
     
   }

   /**
    * Create filter
    *
    * @return The filter
    */
   @Override
   protected Filter createFilter()
   {
      return new KeyFilter();
   }
}

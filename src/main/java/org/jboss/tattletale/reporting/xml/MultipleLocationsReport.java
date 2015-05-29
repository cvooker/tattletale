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
import org.jboss.tattletale.reporting.abstracts.MultipleLocationsReportAbstract;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.jboss.tattletale.reporting.common.*;
import org.jboss.tattletale.reporting.interfaces.*;
import org.jboss.tattletale.reporting.common.*;
/**
 * Multiple locations report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public class MultipleLocationsReport extends MultipleLocationsReportAbstract
{
   /** NAME */
   private static final String NAME = "Multiple Locations";

   /** DIRECTORY */
   private static final String DIRECTORY = "multiplelocations";


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

      for (Archive a : archives)
      {
         if (a instanceof NestableArchive)
         {
            NestableArchive nestableArchive = (NestableArchive) a;
            recursivelyWriteContent(bw, nestableArchive.getSubArchives());
         }
         else if (a.getLocations().size() > 1)
         {
            boolean filtered = isFiltered(a.getName());

            if (!filtered)
            {
               status = ReportStatus.YELLOW;
            }

            if (odd)
            {
               bw.write("  <element class=\"rowodd\">" + Dump.newLine());
            }
            else
            {
               bw.write("  <element class=\"roweven\">" + Dump.newLine());
            }
            bw.write("     <Name>../jar/" + a.getName() + ".xml" +
                     a.getName() + "</Name>" + Dump.newLine());
            if (!filtered)
            {
               bw.write("     <Location>");
            }
            
            Iterator<Location> lit = a.getLocations().iterator();
            while (lit.hasNext())
            {
               Location location = lit.next();
               bw.write(location.getFilename());

               if (lit.hasNext())
               {
                  bw.write("<br>");
               }
            }

            bw.write("</Location>" + Dump.newLine());
            bw.write("  </element>" + Dump.newLine());

            odd = !odd;
         }
      }

   }

   /**
    * write out the header of the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an errror occurs
    */
   public void writeBodyHeader(BufferedWriter bw) throws IOException
   {
      bw.write("<reporting>" + Dump.newLine());
      bw.write(Dump.newLine());

      bw.write("<h1>" + NAME + "</h1>" + Dump.newLine());

      bw.write("../index.xml>" + Dump.newLine());
     
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

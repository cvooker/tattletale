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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

import org.jboss.tattletale.reporting.abstracts.AbstractReport;
import org.jboss.tattletale.reporting.abstracts.MultipleJarsReportAbstract;
import org.jboss.tattletale.reporting.common.*;
import org.jboss.tattletale.reporting.interfaces.*;
/**
 * Multiple jars report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public class MultipleJarsReport extends MultipleJarsReportAbstract
{
   /** NAME */
   private static final String NAME = "Multiple Jar files";

   /** DIRECTORY */
   private static final String DIRECTORY = "multiplejars";

   /** Globally provides */
   private SortedMap<String, SortedSet<String>> gProvides;

  
   /**
    * Set the globally provides map to be used in generating this report
    *
    * @param gProvides the map of global provides
    */
   public void setGlobalProvides(SortedMap<String, SortedSet<String>> gProvides)
   {
      this.gProvides = gProvides;
   }


   /**
    * write out the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    */
   public void writeBodyContent(BufferedWriter bw) throws IOException
   {
      bw.write("<elements>" + Dump.newLine());


      //bw.write("     <th>Class</th>" + Dump.newLine());
      //bw.write("     <th>Jar files</th>" + Dump.newLine());
   
      boolean odd = true;

      for (Map.Entry<String, SortedSet<String>> entry : gProvides.entrySet())
      {

         String clz = entry.getKey();
         SortedSet archives = entry.getValue();

         if (archives.size() > 1)
         {
            boolean filtered = isFiltered(clz);
            if (!filtered)
            {
               status = ReportStatus.RED;
            }

            if (odd)
            {
               bw.write("  <element class=\"rowodd\">" + Dump.newLine());
            }
            else
            {
               bw.write("  <element class=\"roweven\">" + Dump.newLine());
            }
            bw.write("     <Class>" + clz + "</Class>" + Dump.newLine());
          
            
            bw.write("     <Jar_file>");
            
            

            Iterator sit = archives.iterator();
            while (sit.hasNext())
            {
               String archive = (String) sit.next();
               bw.write("../jar/" + archive + ".xml" + archive + Dump.newLine());

               if (sit.hasNext())
               {
                  bw.write(", ");
               }
            }

            bw.write("</Jar_file>" + Dump.newLine());
            bw.write("  </element>" + Dump.newLine());

            odd = !odd;
         }
      }

      bw.write("</elements>" + Dump.newLine());
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

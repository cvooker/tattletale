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
import org.jboss.tattletale.reporting.abstracts.AbstractReport;
import org.jboss.tattletale.reporting.abstracts.SealedReportAbstract;

import java.io.BufferedWriter;
import java.io.IOException;

import org.jboss.tattletale.reporting.common.*;
import org.jboss.tattletale.reporting.interfaces.*;
/**
 * Sealed information report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public class SealedReport extends SealedReportAbstract
{
   /** NAME */
   private static final String NAME = "Sealed information";

   /** DIRECTORY */
   private static final String DIRECTORY = "sealed";
   /**
    * write out the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    */
   public void writeBodyContent(BufferedWriter bw) throws IOException
   {
      bw.write("<elements>" + Dump.newLine());

    
      //bw.write("     <th>Archive</th>" + Dump.newLine());
      //bw.write("     <th>Status</th>" + Dump.newLine());
      

      boolean odd = true;

      int sealed = 0;
      int unsealed = 0;

      for (Archive archive : archives)
      {

         String archiveName = archive.getName();
         int finalDot = archiveName.lastIndexOf(".");
         String extension = archiveName.substring(finalDot + 1);
          
         bw.write("<element>" + Dump.newLine());
         
        
         bw.write("     <Archive>../" + extension + "/" + archiveName + ".xml>" + archiveName
                  + "</Archive>" + Dump.newLine());
         if (archive.hasManifestKey("Sealed")
               && Boolean.TRUE.equals(Boolean.valueOf(archive.getManifestValue("Sealed"))))
         {
            bw.write("     <Status>Sealed</Status>" + Dump.newLine());
            sealed++;
         }
         else
         {
            bw.write("     <Status>Unsealed</Status>" + Dump.newLine());
            unsealed++;
         }
         bw.write("  </element>" + Dump.newLine());

         odd = !odd;
      }

      bw.write("</elements>" + Dump.newLine());

      boolean filtered = isFiltered();
      if (sealed > 0 && unsealed > 0 && !filtered)
      {
         status = ReportStatus.YELLOW;
      }

      bw.write(Dump.newLine());
     

      bw.write("<elements>" + Dump.newLine());

     
      //bw.write("     <th>Status</th>" + Dump.newLine());
      //bw.write("     <th>Archives</th>" + Dump.newLine());
    

      bw.write("  <element>" + Dump.newLine());
      bw.write("     <Status>Sealed</Status>" + Dump.newLine());
      if (!filtered)
      {
         bw.write("     <Archives>" + sealed + "</Archives>" + Dump.newLine());
      }
      else
      {
         bw.write("     <Archives>" + sealed + "</Archives>" + Dump.newLine());
      }
      bw.write("  </element>" + Dump.newLine());

      bw.write("  <element>" + Dump.newLine());
      bw.write("     <value>Unsealed</value>" + Dump.newLine());
      if (!filtered)
      {
         bw.write("     <value>" + unsealed + "</value>" + Dump.newLine());
      }
      else
      {
         bw.write("     <value>"
                  + unsealed + "</value>" + Dump.newLine());
      }
      bw.write("  </element>" + Dump.newLine());

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
      return new BooleanFilter();
   }
}

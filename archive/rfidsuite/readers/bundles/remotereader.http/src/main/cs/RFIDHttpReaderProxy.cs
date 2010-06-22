/*
 * Copyright 2005-2008, Aspire
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation (the "LGPL"); either version 2.1 of the 
 * License, or (at your option) any later version. If you do not alter this 
 * notice, a recipient may use your version of this file under either the 
 * LGPL version 2.1, or (at his option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
 * KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;

namespace OW2.AspireRfid
{
    class Example
    {
        static void Main(string[] args)
        {
            RFIDHttpReaderProxy reader = new RFIDHttpReaderProxy("localhost", 8080);
            reader.Submit("35654321000000112345678B", DateTime.Now, "myreader");
            Console.ReadKey();
        }
    }
    /// <summary>
    /// Simple proxy for submitting RFID reading from .NET applications into the RFID Suite
    /// </summary>
    class RFIDHttpReaderProxy
    {
        private String baseURI;
        private DateTime javaInitialDate = new DateTime(1970, 01, 01);

        /// <summary>
        /// Constructs a reader instance reusable in different requests
        /// 
        /// </summary>
        /// <param name="address"></param>
        /// <param name="port"></param>
        public RFIDHttpReaderProxy(String address, int port) 
        {
            this.baseURI = "http://" + address + ":" + port + "/HttpTagReader";
        }

        /// <summary>
        /// 
        /// Submits the info the the
        /// </summary>
        /// <param name="tagId">The id of the read tag</param>
        /// <param name="timestamp">Timestamp of the reading</param>
        /// <param name="readerid">String which identifies the reader</param>
        public void Submit(String tagId, DateTime timestamp, String readerid)
        {
            StringBuilder sb = new StringBuilder();
            sb.Append(baseURI);
            sb.Append("?id=");
            sb.Append(System.Web.HttpUtility.UrlEncode(tagId));
            sb.Append("&timestamp=");
            sb.Append(System.Web.HttpUtility.UrlEncode(getJavaCompatibleTimestamp(timestamp)));
            sb.Append("&readerid=");
            sb.Append(System.Web.HttpUtility.UrlEncode(readerid));


            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(sb.ToString());
            request.Method = "GET";
            WebResponse response = request.GetResponse();
            
        }
        /// <summary>
        /// Adapts the .NET nanosecond tick (which begins in 1/1/0001 00:00:00) to Java milliseconds (counting from 1/1/1970 00:00:00)
        /// 
        /// </summary>
        /// <param name="timestamp">The .NET timestamp</param>
        /// <returns>A String which represents the milliseconds understood by the Java Platform</returns>
        private String getJavaCompatibleTimestamp(DateTime timestamp)
        {
            return Convert.ToString((timestamp.Ticks - (javaInitialDate.Ticks)) / 10000);
        }

    }
}

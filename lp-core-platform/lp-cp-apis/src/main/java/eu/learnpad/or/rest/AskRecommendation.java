/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
package eu.learnpad.or.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import eu.learnpad.exception.LpRestException;

// 
@Path("/learnpad/or/recommendation/{modelsetid}")
public interface AskRecommendation {
	/**
	 * 
	 * The file returned could be XML or JSON (below is shown as XML to explain the structure)
	 * The different types of recommendations are based on the Ölten's workshop discussions
	 * http://wiki.learnpad.eu/LearnPAdWiki/bin/view/WP5/MinutesOltenWorkshop#HContextpanel 
	 * 
	 * <recommendations>
	 *   <recommendation type="role">
	 *     content
	 *   </recommendation>
	 *   <recommendation type="context">
	 *     content
	 *   </recommendation>
	 *   ...
	 * </recommendations>
	 * 
	 * The different type of recommendations could be:
	 * - role: about the role concerned by the current artifact (what people?, what organisation unit?)
	 * - context: mainly in execution mode, what are the contextual information from previous steps that could be needed here
	 * - expert: recommend some other people that may help on the current artifact
	 * - resource: other document that can complete the information on the current artifact
	 * 
	 * @param modelSetId is the uniq ID of the model set
	 * @param artifactId is the ID of the artifact in the model (event, gateway, unit, etc.)
	 * @return is the list of recommendations (see above for the format)
	 * @throws LpRestException
	 */
	@GET
	byte[] askRecommendation(@PathParam("modelsetid") String modelSetId,
			@QueryParam("artifactId") String artifactId) throws LpRestException;
}

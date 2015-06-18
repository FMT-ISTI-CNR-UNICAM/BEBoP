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
package eu.learnpad.core.factory;

import eu.learnpad.or.Controller;

public class LpXwikiControllersFactory extends LpControllersFactory {

	@Override
	public eu.learnpad.cw.Controller createControllerCW() {
		return new eu.learnpad.core.impl.cw.XwikiController();
	}

	@Override
	public eu.learnpad.me.Controller createControllerME() {
		return new eu.learnpad.core.impl.me.XwikiController();
	}

	@Override
	public eu.learnpad.qm.Controller createControllerQM() {
		return new eu.learnpad.core.impl.qm.XwikiController();
	}

	@Override
	public Controller createControllerOR() {
		return new eu.learnpad.core.impl.or.XwikiController();
	}

}

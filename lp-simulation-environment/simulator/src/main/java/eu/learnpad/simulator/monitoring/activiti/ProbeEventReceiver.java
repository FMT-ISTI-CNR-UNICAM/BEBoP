/**
 *
 */
package eu.learnpad.simulator.monitoring.activiti;

import java.util.ArrayList;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.naming.NamingException;

import eu.learnpad.sim.rest.event.impl.ProcessEndEvent;
import eu.learnpad.sim.rest.event.impl.ProcessStartEvent;
import eu.learnpad.sim.rest.event.impl.SessionScoreUpdateEvent;
import eu.learnpad.sim.rest.event.impl.SimulationEndEvent;
import eu.learnpad.sim.rest.event.impl.SimulationStartEvent;
import eu.learnpad.sim.rest.event.impl.TaskEndEvent;
import eu.learnpad.sim.rest.event.impl.TaskStartEvent;
import eu.learnpad.simulator.IProcessEventReceiver;
import eu.learnpad.simulator.IProcessManager;
import eu.learnpad.simulator.mon.event.GlimpseBaseEvent;
import eu.learnpad.simulator.mon.event.GlimpseBaseEventBPMN;
import eu.learnpad.simulator.mon.probe.GlimpseAbstractProbe;
import eu.learnpad.simulator.mon.utils.Manager;
import eu.learnpad.simulator.monitoring.event.impl.ProcessEndSimEvent;
import eu.learnpad.simulator.monitoring.event.impl.ProcessStartSimEvent;
import eu.learnpad.simulator.monitoring.event.impl.SessionScoreUpdateSimEvent;
import eu.learnpad.simulator.monitoring.event.impl.SimulationEndSimEvent;
import eu.learnpad.simulator.monitoring.event.impl.SimulationStartSimEvent;
import eu.learnpad.simulator.monitoring.event.impl.TaskEndSimEvent;
import eu.learnpad.simulator.monitoring.event.impl.TaskStartSimEvent;

/*
 * #%L
 * LearnPAd Simulator
 * %%
 * Copyright (C) 2014 - 2015 Linagora
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

/**
 * Monitor simulation sessions in order to notify Glimpse server of various
 * events
 *
 * @author Tom Jorquera - Linagora
 *
 */
public class ProbeEventReceiver extends GlimpseAbstractProbe implements
IProcessEventReceiver {

	private static final String GLIMPSE_CONF_PATH = "glimpse_server.conf";

	private static String getServerAddress() {
		Scanner scan = new Scanner(ProbeEventReceiver.class.getClassLoader()
				.getResourceAsStream(GLIMPSE_CONF_PATH));
		String uiPage = scan.useDelimiter("\\Z").next();
		scan.close();
		return uiPage;
	}

	private final IProcessManager manager;

	/**
	 * @param settings
	 */
	public ProbeEventReceiver(IProcessManager manager) {
		super(Manager.createProbeSettingsPropertiesObject(
				"org.apache.activemq.jndi.ActiveMQInitialContextFactory",
				ProbeEventReceiver.getServerAddress(), "system", "manager",
				"TopicCF", "jms.probeTopic", false, "probeName", "probeTopic"));

		this.manager = manager;
	}

	/**
	 * Send a notification to the monitor
	 *
	 * @param event
	 */
	protected void send(GlimpseBaseEventBPMN<String> event) {
		try {
			this.sendEventMessage(event, false);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.activiti.engine.delegate.event.ActivitiEventListener#isFailOnException
	 * ()
	 */
	public boolean isFailOnException() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * it.cnr.isti.labsedc.glimpse.probe.GlimpseAbstractProbe#sendMessage(it
	 * .cnr.isti.labsedc.glimpse.event.GlimpseBaseEvent, boolean)
	 */
	@Override
	public void sendMessage(GlimpseBaseEvent<?> event, boolean debug) {
		// nothing to do here(?)
	}

	@Override
	public void receiveSimulationStartEvent(SimulationStartSimEvent event) {
		GlimpseBaseEventBPMN<String> monitoringEvent = new GlimpseBaseEventBPMN<String>(
				"Activity_" + event.timestamp,
				event.simulationsessionid,
				event.timestamp,
				event.getType().toString(),
				false,
				"",
				new SimulationStartEvent(
						event.timestamp,
						event.simulationsessionid,
						new ArrayList<String>(event.involvedusers),
						manager.getModelSetId(event.initialProcessDefinitionKey),
						manager.getSimulationSessionParametersData(event.simulationsessionid)));

		send(monitoringEvent);

	}

	@Override
	public void receiveSimulationEndEvent(SimulationEndSimEvent event) {
		GlimpseBaseEventBPMN<String> monitoringEvent = new GlimpseBaseEventBPMN<String>(
				"Activity_" + event.timestamp,
				event.simulationsessionid,
				event.timestamp,
				event.getType().toString(),
				false,
				"",
				new SimulationEndEvent(
						event.timestamp,
						event.simulationsessionid,
						new ArrayList<String>(event.involvedusers),
						manager.getModelSetId(event.simulationsessionid),
						manager.getSimulationSessionParametersData(event.simulationsessionid)));

		send(monitoringEvent);

	}

	@Override
	public void receiveProcessStartEvent(ProcessStartSimEvent event) {
		GlimpseBaseEventBPMN<String> monitoringEvent = new GlimpseBaseEventBPMN<String>(
				"Activity_" + event.timestamp,
				event.simulationsessionid,
				event.timestamp,
				event.getType().toString(),
				false,
				"",
				new ProcessStartEvent(
						event.timestamp,
						event.simulationsessionid,
						new ArrayList<String>(event.involvedusers),
						manager.getModelSetId(event.simulationsessionid),
						manager.getSimulationSessionParametersData(event.simulationsessionid),
						event.processInstance.processartifactid,
						event.processInstance.processartifactkey));

		send(monitoringEvent);

	}

	@Override
	public void receiveProcessEndEvent(ProcessEndSimEvent event) {
		GlimpseBaseEventBPMN<String> monitoringEvent = new GlimpseBaseEventBPMN<String>(
				"Activity_" + event.timestamp,
				event.simulationsessionid,
				event.timestamp,
				event.getType().toString(),
				false,
				"",
				new ProcessEndEvent(
						event.timestamp,
						event.simulationsessionid,
						new ArrayList<String>(event.involvedusers),
						manager.getModelSetId(event.simulationsessionid),
						manager.getSimulationSessionParametersData(event.simulationsessionid),
						event.processInstance.processartifactid,
						event.processInstance.processartifactkey));
		send(monitoringEvent);

	}

	@Override
	public void receiveTaskStartEvent(TaskStartSimEvent event) {
		GlimpseBaseEventBPMN<String> monitoringEvent = new GlimpseBaseEventBPMN<String>(
				"Activity_" + event.timestamp,
				event.simulationsessionid,
				event.timestamp,
				event.getType().toString(),
				false,
				event.task.subprocessKey,
				new TaskStartEvent(
						event.timestamp,
						event.simulationsessionid,
						new ArrayList<String>(event.involvedusers),
						manager.getModelSetId(event.simulationsessionid),
						manager.getSimulationSessionParametersData(event.simulationsessionid),
						event.task.processId, event.task.id, event.task.key,
						new ArrayList<String>(event.involvedusers)));

		send(monitoringEvent);

	}

	@Override
	public void receiveTaskEndEvent(TaskEndSimEvent event) {
		GlimpseBaseEventBPMN<String> monitoringEvent = new GlimpseBaseEventBPMN<String>(
				"Activity_" + event.timestamp,
				event.simulationsessionid,
				event.timestamp,
				event.getType().toString(),
				false,
				event.task.subprocessKey,
				new TaskEndEvent(
						event.timestamp,
						event.simulationsessionid,
						new ArrayList<String>(event.involvedusers),
						manager.getModelSetId(event.simulationsessionid),
						manager.getSimulationSessionParametersData(event.simulationsessionid),
						event.task.processId, event.task.id, event.task.key,
						new ArrayList<String>(event.involvedusers),
						event.completingUser, event.submittedData));

		send(monitoringEvent);

	}

	@Override
	public void receiveSessionScoreUpdateEvent(SessionScoreUpdateSimEvent event) {
		GlimpseBaseEventBPMN<String> monitoringEvent = new GlimpseBaseEventBPMN<String>(
				"Activity_" + event.timestamp,
				event.simulationsessionid,
				event.timestamp,
				event.getType().toString(),
				false,
				"",
				new SessionScoreUpdateEvent(
						event.timestamp,
						event.simulationsessionid,
						new ArrayList<String>(event.involvedusers),
						manager.getModelSetId(event.simulationsessionid),
						manager.getSimulationSessionParametersData(event.simulationsessionid),
						event.processid, event.user, event.sessionscore));

		send(monitoringEvent);

	}

}